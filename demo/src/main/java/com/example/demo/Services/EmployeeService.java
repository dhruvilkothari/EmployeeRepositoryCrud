package com.example.demo.Services;

import com.example.demo.Advices.ApiError;
import com.example.demo.Advices.ApiResponse;
import com.example.demo.Dto.EmployeeDto;
import com.example.demo.EmployeeRepository.EmployeeRepository;
import com.example.demo.Entities.EmployeeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.Reflection;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RedisTemplate<Long,Object> redisTemplate;
    private final ModelMapper modelMapper;
    
    public ResponseEntity<ApiResponse<?>> getEmployeeById(Long id) {
        try{
            Object o = redisTemplate.opsForValue().get(id);
            EmployeeEntity employeeEntity = null;
            if(o instanceof EmployeeEntity){
                 employeeEntity = (EmployeeEntity)o;
            }
            else {
             employeeEntity = employeeRepository.findById(id).orElseThrow(() -> new Exception("User Not Found With This Id"));

            }

            EmployeeDto employeeDto = modelMapper.map(employeeEntity, EmployeeDto.class);
            ApiResponse<EmployeeDto> apiResponse = new ApiResponse<>(employeeDto);
            redisTemplate.opsForValue().set(id,employeeEntity);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);

        }catch (Exception exp){
            ApiError apiError = new ApiError(exp.getMessage(), HttpStatus.BAD_REQUEST);
            ApiResponse<ApiError> apiResponse = new ApiResponse<>(apiError);
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees() {
        List<EmployeeDto> collect = employeeRepository.findAll().stream().map(employeeEntity -> {
            return modelMapper.map(employeeEntity, EmployeeDto.class);
        }).toList();
        ApiResponse<List<EmployeeDto>> apiResponse = new ApiResponse<>(collect);
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<ApiResponse<?>> deleteEmployeeById(Long id) {
        try{
            boolean b = employeeRepository.existsById(id);
            if(!b){
                throw new Exception("Employee with Given Id = {} not found");
            }
            employeeRepository.deleteById(id);
            ApiResponse<String>apiResponse = new ApiResponse<>("User Deleted By This Id: "+id);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception exp){
            ApiResponse<ApiError> apiResponse = new ApiResponse<>(new ApiError(exp.getMessage(),HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<?>> createEmployee(EmployeeDto employeeDto) {
        try{

            EmployeeEntity employeeEntity = modelMapper.map(employeeDto, EmployeeEntity.class);
            EmployeeDto savedDto = modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDto.class);
            ApiResponse<EmployeeDto> apiResponse = new ApiResponse<>(savedDto);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception exp){
            ApiResponse<ApiError> apiResponse = new ApiResponse<>(new ApiError(exp.getMessage(),HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<?>> patchEmployeeById(Long id, HashMap<Object, Object> map) {
        try{
            EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(() -> new Exception("User Not Found With This Id"));
            map.forEach((key,value)->{
                Field field = ReflectionUtils.findField(EmployeeEntity.class,key.toString());
                if(field!=null){
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, employeeEntity, value);
                }

            });
            EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
            EmployeeDto employeeDto = modelMapper.map(savedEmployeeEntity, EmployeeDto.class);
            ApiResponse<EmployeeDto> apiResponse = new ApiResponse<>(employeeDto);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);


        }catch (Exception exp){
            ApiError apiError = new ApiError(exp.getMessage(), HttpStatus.BAD_REQUEST);
            ApiResponse<ApiError> apiResponse = new ApiResponse<>(apiError);
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
    }
}
