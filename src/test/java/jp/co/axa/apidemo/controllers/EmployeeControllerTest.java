package jp.co.axa.apidemo.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.RequestEmployee;
import jp.co.axa.apidemo.services.EmployeeService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    EmployeeController employeeController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetEmployees() throws Exception {
        Employee employee1 = Employee.builder()
                .id(1L)
                .name("test1")
                .salary(100)
                .department("IT")
                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .name("test2")
                .salary(200)
                .department("Sales")
                .build();
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeService.retrieveEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployees();
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().get(0)).isEqualTo(employee1);
        assertThat(response.getBody().get(1)).isEqualTo(employee2);
    }

    @Test
    public void testGetEmployee() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("test1")
                .salary(100)
                .department("IT")
                .build();
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployee(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo(employee);
    }

    @Test
    public void testSaveEmployee() {
        RequestEmployee request = RequestEmployee.builder()
                .name("test1")
                .salary(100)
                .department("IT")
                .build();
        Employee employee = Employee.builder()
                .name("test1")
                .salary(100)
                .department("IT")
                .build();
        when(employeeService.saveEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.saveEmployee(request);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody()).isEqualTo(employee);
    }

    @Test
    public void testDeleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(anyLong());
        ResponseEntity<String> response = employeeController.deleteEmployee(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testUpdateEmployee200() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("oldTest")
                .salary(200)
                .department("Sales")
                .build();
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);

        Employee newEmployee = Employee.builder()
                .id(1L)
                .name("newTest")
                .salary(300)
                .department("IT")
                .build();
        when(employeeService.updateEmployee(newEmployee)).thenReturn(newEmployee);

        RequestEmployee request = RequestEmployee.builder()
                .name("newTest")
                .salary(300)
                .department("IT")
                .build();

        ResponseEntity<String> response = employeeController.updateEmployee(request, 1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo("Successfully updated.");
    }

    @Test
    public void testUpdateEmployee204() {
        when(employeeService.getEmployee(anyLong())).thenReturn(null);

        RequestEmployee request = RequestEmployee.builder()
                .name("newTest")
                .salary(300)
                .department("IT")
                .build();

        ResponseEntity<String> response = employeeController.updateEmployee(request, 2L);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getBody()).isEqualTo("Target ID does not exist.");
    }
}
