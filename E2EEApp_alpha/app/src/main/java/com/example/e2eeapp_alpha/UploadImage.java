package com.example.e2eeapp_alpha;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ThrowOnExtraProperties;

@IgnoreExtraProperties
@ThrowOnExtraProperties
public class UploadImage {
    public String name;
    public String uri;

    public UploadImage(){}
    public UploadImage(String iname, String iURI){
        if (iname.trim().equals("")){
            iname = "No Name";
        }
        this.name = iname;
        this.uri = iURI;
    }

    public String getName(){
        return name;
    }

    public String getURI(){
        return uri;
    }

    public void setImageName(String imageName) {
        this.name = imageName;
    }

    public void setImageURI(String imageURI) {
        this.uri = imageURI;
    }
}
