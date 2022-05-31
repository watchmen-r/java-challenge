package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.RequestEmployee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Employee> saveEmployee(@RequestBody @Validated RequestEmployee request) {
        // TODO もっといい書き方あるはず
        Employee employee = new Employee();
        request.setName(employee.getName());
        request.setSalary(employee.getSalary());
        request.setDepartment(employee.getDepartment());
        Employee savedEmployee = employeeService.saveEmployee(employee);

        return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee,
            @PathVariable(name = "employeeId") Long employeeId) {
        Employee existEmployee = employeeService.getEmployee(employeeId);

        if (existEmployee != null) {
            // TODO もっといい書き方あるはず
            Employee newEmployee = new Employee();
            newEmployee.setId(employeeId);
            newEmployee.setName(employee.getName());
            newEmployee.setSalary(employee.getSalary());
            newEmployee.setDepartment(employee.getDepartment());
            employeeService.updateEmployee(newEmployee);
            return new ResponseEntity<String>("Updated", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

    }

}
