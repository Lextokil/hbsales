package br.com.hbsis.pedidos;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.FuncionarioDTO;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.itempedido.InvoiceItemDTOSet;
import br.com.hbsis.itempedido.ItemPedido;
import br.com.hbsis.itempedido.ItemPedidoDTO;
import br.com.hbsis.itempedido.ItemPedidoService;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import br.com.hbsis.periodovendas.IPeriodoVendasRepository;
import br.com.hbsis.periodovendas.PeriodoVendas;
import br.com.hbsis.produtos.Produto;
import br.com.hbsis.produtos.ProdutoService;
import br.com.hbsis.util.CodeManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository iPedidoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;
    private final FornecedorService fornecedorService;
    private final LinhaCategoriaService linhaCategoriaService;
    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FuncionarioService funcionarioService;

    public PedidoService(IPedidoRepository iPedidoRepository, ProdutoService produtoService,
                         ItemPedidoService itemPedidoService, FornecedorService fornecedorService, LinhaCategoriaService linhaCategoriaService, IPeriodoVendasRepository iPeriodoVendasRepository, FuncionarioService funcionarioService) {
        this.iPedidoRepository = iPedidoRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
        this.fornecedorService = fornecedorService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.funcionarioService = funcionarioService;
    }


    public PedidoDTO save(PedidoDTO pedidoDTO, Long idFornecedor, Long idFuncionario) {
        Fornecedor fornecedorDoProduto = fornecedorService.findFornecedorById(idFornecedor);
        FuncionarioDTO funcionarioDoPedido = funcionarioService.findById(idFuncionario);

        Set<ItemPedido> itemPedidoCompletos = this.parseToItemPedido(pedidoDTO.getItemPedidoDTO(), fornecedorDoProduto);
        pedidoDTO.setStatus(StatusPedido.ATIVO.getDescricao());
        this.validate(pedidoDTO, fornecedorDoProduto);
        LOGGER.info("Salvando Pedido");
        LOGGER.debug("Produto: {}", pedidoDTO);


        double valorTotal = valorTotalPedido(itemPedidoCompletos);
        LocalDateTime hoje = LocalDateTime.now();
        String codigo = CodeManager.generateProdutoCode(pedidoDTO.getCodigo());

        Pedido pedido = new Pedido(itemPedidoCompletos, pedidoDTO.getStatus(), hoje, codigo, valorTotal);

        for (ItemPedido itemPedido : pedido.getItensPedido()) {
            itemPedido.setPedido(pedido);
        }

        informInvoice(pedido, funcionarioDoPedido, fornecedorDoProduto);
        pedido = this.iPedidoRepository.save(pedido);
        return PedidoDTO.of(pedido);
    }

    public double valorTotalPedido(Set<ItemPedido> itensPedido) {
        Double valorTotal = 0.0;
        for (ItemPedido ip : itensPedido) {
            Produto produtoDoPedido = produtoService.findProdutoById(ip.getProduto().getId());
            valorTotal += (produtoDoPedido.getPrecoProduto() * ip.getQuantidade());

        }
        return valorTotal;
    }


    public Set<ItemPedido> saveItensPedido(Set<ItemPedido> itensPedido, Pedido pedido) {
        itensPedido.forEach(ip -> ip.setPedido(pedido));
        return this.itemPedidoService.saveAll(itensPedido);
    }

    public Set<ItemPedido> parseToItemPedido(Set<ItemPedidoDTO> itemPedidosDTO, Fornecedor fornecedor) {
        Set<ItemPedido> itemPedidoSet = new HashSet<>();
        for (ItemPedidoDTO ipDTO : itemPedidosDTO) {
            Produto produtoDoItem = produtoService.findProdutoById(ipDTO.getIdProduto());
            if (produtoDoItem.getLinhaCategoria().getCategoriaProduto().getFornecedor() == fornecedor) {
                ItemPedido itemPedido = new ItemPedido();

                itemPedido.setProduto(produtoService.findProdutoById(ipDTO.getIdProduto()));

                itemPedido.setQuantidade(ipDTO.getQuantidade());
                itemPedidoSet.add(itemPedido);
            } else {
                LOGGER.info(
                        "Produto: " + produtoDoItem.getNomeProduto() + " não pertence ao fornecedor informado e foi removido do pedido"
                );
            }
        }
        if(itemPedidoSet.isEmpty()){
            throw new IllegalArgumentException("Nenhum produto desejado pertence ao fornecedor informado");
        }else{
            return itemPedidoSet;
        }

    }


    private void validate(PedidoDTO pedidoDTO, Fornecedor fornecedor) {
        LOGGER.info("Validando Pedido");

        List<PeriodoVendas> periodosDoFornecedor = iPeriodoVendasRepository.findPeriodoByFornecedor(fornecedor.getId());
        if (!verificarVigenciaPeriodoVendas(periodosDoFornecedor)) {
            throw new IllegalArgumentException("Fornecedor informado está em um periodo de vendas vigente");
        }
        if (pedidoDTO == null) {
            throw new IllegalArgumentException("Pedido não deve ser nula");
        }

        if (pedidoDTO.getItemPedidoDTO().isEmpty()) {
            throw new IllegalArgumentException("Pedido tem que ter pelo menos um produto do mesmo fornecedor");
        }
        if (!EnumUtils.isValidEnum(StatusPedido.class, pedidoDTO.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Status inválido");
        }
         if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getItemPedidoDTO().size() < 1))) {
            throw new IllegalArgumentException("Pedido tem que ter produtos");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getCodigo().length() > 10))) {
            throw new IllegalArgumentException("Codigo não deve ter mais de 10 caracteres");
        }

    }

    public PedidoDTO findById(Long id) {
        Optional<Pedido> pedidos = this.iPedidoRepository.findById(id);

        if (pedidos.isPresent()) {
            return PedidoDTO.of(pedidos.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<PedidoDTO> findAll() {

        List<Pedido> pedidos = iPedidoRepository.findAll();
        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        pedidos.forEach(pedido -> pedidosDTO.add(PedidoDTO.of(pedido)));

        return pedidosDTO;
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);


        if (pedidoOptional.isPresent()) {
            Pedido pedidoExistente = pedidoOptional.get();
            Fornecedor fornecedor = new Fornecedor();
            LOGGER.info("Atualizando pedido... id: [{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoExistente.toString());


            pedidoExistente.setItensPedido((this.parseToItemPedido(pedidoDTO.getItemPedidoDTO(), fornecedor)));
            pedidoExistente.setValorTotal(pedidoDTO.getValorTotal());

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);

            return PedidoDTO.of(pedidoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }

    public boolean verificarVigenciaPeriodoVendas(List<PeriodoVendas> periodos) {
        LocalDateTime hoje = LocalDateTime.now();
        for (PeriodoVendas pv : periodos) {
            if (hoje.isBefore(pv.getDataFinal().plusDays(1)) && hoje.isAfter(pv.getDataInicio().minusDays(1))) {
                return true;
            }
        }
        return false;
    }

    private void informInvoice(Pedido pedido, FuncionarioDTO funcionarioDoPedido, Fornecedor fornecedor) {
        // request url
        String url = "http://nt-04053:9999/api/invoice";

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "f59fe89a-1b67-11ea-978f-2e728ce88125");
        Set<InvoiceItemDTOSet> invoiceItemDTOSets = parseToInvoiceRequest(pedido.getItensPedido());

        InvoiceDTO invoiceDTO = new InvoiceDTO(fornecedor.getCnpj(),funcionarioDoPedido.getUuid()
                                                                        ,invoiceItemDTOSets,pedido.getValorTotal());
        // build the request
        HttpEntity<InvoiceDTO> request = new HttpEntity<>(invoiceDTO, headers);

        // send POST request
        ResponseEntity<InvoiceDTO> response = restTemplate.postForEntity(url, request, InvoiceDTO.class);
        LOGGER.info(response.toString());
    }
    private Set<InvoiceItemDTOSet> parseToInvoiceRequest(Set<ItemPedido>itemPedidos){
        Set<InvoiceItemDTOSet> invoiceItemDTOSets = new HashSet<>();
        for (ItemPedido ip :itemPedidos) {
            InvoiceItemDTOSet invoiceItemDTOSet = new InvoiceItemDTOSet(ip.getQuantidade(),ip.getProduto().getNomeProduto());
            invoiceItemDTOSets.add(invoiceItemDTOSet);
        }
        return invoiceItemDTOSets;
    }
}
