package com.example.demo.Controller;

import com.example.demo.Advices.ApiResponse;
import com.example.demo.Dto.EmployeeDto;
import com.example.demo.Services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/employee")

@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getEmployeeById(@PathVariable(required = true) Long id){
        return employeeService.getEmployeeById(id);
    }
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteEmployeeById(@PathVariable(required = true) Long id){
        return employeeService.deleteEmployeeById(id);
    }
    @PostMapping("/createEmployee")
    public ResponseEntity<ApiResponse<?>>createEmployee(@RequestBody(required = true) EmployeeDto employeeDto){
        return employeeService.createEmployee(employeeDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>>patchEmployeeById(@PathVariable(required = true) Long id, @RequestBody(required = true)HashMap<Object,Object> map){
        return employeeService.patchEmployeeById(id,map);
    }





}
