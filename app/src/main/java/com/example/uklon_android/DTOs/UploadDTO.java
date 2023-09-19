package com.example.uklon_android.DTOs;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class UploadDTO {
    String userId;
    MultipartBody.Part imageFile;

    public void setUserId(String userId)
    {
        this.userId = userId;
    };

    public String getUserId()
    {
        return userId;
    }

    public void setImageFile(MultipartBody.Part imageFile)
    {
        this.imageFile = imageFile;
    };

    public MultipartBody.Part getImageFile()
    {
        return imageFile;
    }
}
