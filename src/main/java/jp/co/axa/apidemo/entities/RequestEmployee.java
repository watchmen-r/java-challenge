package jp.co.axa.apidemo.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestEmployee {
    @NotBlank(message = "please enter a name")
    private String name;

    @NotNull(message = "please enter a salary")
    private Integer salary;

    @NotBlank(message = "please enter a department")
    private String department;
}
