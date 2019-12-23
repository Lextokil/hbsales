package br.com.hbsis.funcionario;


public class FuncionarioResponseDTO {
    private String employeeUuid;
    private String employeeName;

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                ", Nome ='" + getEmployeeName() + '\'' +
                ", Uuid ='" + getEmployeeUuid()+ '\'' +
                '}';
    }


}
