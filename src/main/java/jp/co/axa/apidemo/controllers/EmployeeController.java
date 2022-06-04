package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.RequestEmployee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @PostMapping("/employees")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success save new employee data."),
            @ApiResponse(code = 400, message = "Bad request.") })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Employee> saveEmployee(@RequestBody @Validated RequestEmployee request) {
        Employee employee = Employee.builder()
                .name(request.getName())
                .salary(request.getSalary())
                .department(request.getDepartment()).build();
        Employee savedEmployee = employeeService.saveEmployee(employee);

        return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success deletes employee data."),
        @ApiResponse(code = 204, message = "Target ID does not exist.") })
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        Employee existEmployee = employeeService.getEmployee(employeeId);
        if (existEmployee == null) {
            return new ResponseEntity<String>("Target ID does not exist.", HttpStatus.NO_CONTENT);
        }
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success updates employee data."),
            @ApiResponse(code = 204, message = "Target ID does not exist."),
            @ApiResponse(code = 400, message = "Bad request.") })
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody RequestEmployee request,
            @PathVariable(name = "employeeId") Long employeeId) {
        Employee existEmployee = employeeService.getEmployee(employeeId);

        if (existEmployee != null) {
            Employee newEmployee = Employee.builder()
                    .id(employeeId)
                    .name(request.getName())
                    .salary(request.getSalary())
                    .department(request.getDepartment())
                    .build();
            employeeService.updateEmployee(newEmployee, employeeId);
            return new ResponseEntity<String>("Successfully updated.", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Target ID does not exist.", HttpStatus.NO_CONTENT);

    }

}
