package com.example.demo.Advices;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;

@Getter
@Setter
public class ApiResponse<T>  {
    private T data;
    private ApiError apiError;
    private LocalDate timestamp;
    public ApiResponse(){
        this.timestamp = LocalDate.now();
    }
    public ApiResponse(ApiError apiError){
        this();
        this.apiError = apiError;
    }
    public ApiResponse(T data){
        this();
        this.data = data;
    }

}
