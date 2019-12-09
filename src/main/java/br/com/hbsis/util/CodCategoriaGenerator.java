package br.com.hbsis.util;

import br.com.hbsis.categoriaprodutos.CategoriaProduto;
import org.apache.commons.lang.StringUtils;

public class CodCategoriaGenerator {

    public static String codGenerator(CategoriaProduto categoriaProduto) {

        String cnpj = categoriaProduto.getFornecedor().getCnpj();
        cnpj = cnpj.substring(cnpj.length() - 4);
        String dado = StringUtils.leftPad(categoriaProduto.getCodCategoria(), 3, "0");

        return "CAT" + cnpj + dado;


    }

    public static boolean isCodValid(String codigo) {
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

    public static String generateCode(String code){
        String codeGenerated = StringUtils.leftPad(code, 10, "0");
        return  codeGenerated.toUpperCase();
    }

}
