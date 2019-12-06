package br.com.hbsis.util;

import br.com.hbsis.categoriaprodutos.CategoriaProduto;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import org.apache.commons.lang.StringUtils;

public class CodeManager {

    public static String codCategoriaGenerator(CategoriaProduto categoriaProduto) {

        String cnpj = categoriaProduto.getFornecedor().getCnpj();
        cnpj = cnpj.substring(cnpj.length() - 4);
        String dado = StringUtils.leftPad(categoriaProduto.getCodCategoria(), 3, "0");

        return "CAT" + cnpj + dado;


    }

    public static boolean isCodCategoriaValid(String codigo) {
        try {
            if (!(codigo.length() > 3)) {
                Double.parseDouble(codigo);
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String codLinhaGenerator(LinhaCategoriaDTO linhaCategoriaDTO){
        String code = linhaCategoriaDTO.getCodLinha();
        code = code.replaceAll("[^a-zA-Z0-9]+","");
        code = StringUtils.leftPad(code, 10, "0");

        return code.toUpperCase();
    }

}
