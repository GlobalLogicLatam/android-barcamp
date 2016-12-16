package com.globallogic.barcamp.data.repository;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.globallogic.barcamp.data.firebase.callback.DeleteFileStorageCallback;
import com.globallogic.barcamp.data.firebase.callback.GetDownloadUrlFileStorageCallback;
import com.globallogic.barcamp.data.firebase.callback.UploadFileStorageCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */
public class StorageDataRepository {

    private static final String FIREBASE_STORAGE_PATH = "gs://" + FirebaseWorker.FIREBASE_PATH;
    private static final String FIREBASE_STORAGE_FOLDER = "images/";
    private static final String FIREBASE_STORAGE_IMAGES_PATH = FIREBASE_STORAGE_PATH + "/" + FIREBASE_STORAGE_FOLDER;

    public void uploadFile(String localPath, final UploadFileStorageCallback callback) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_STORAGE_PATH);

        Uri file = Uri.fromFile(new File(localPath));
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = "";
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
        final String fileName = file.getLastPathSegment();
        StorageReference fileRef = storageRef.child(FIREBASE_STORAGE_FOLDER + userId + "/" + fileName);

        // start upload
        UploadTask uploadTask = fileRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.onFailureUploadFile(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // format gs://barcamp-b7478.appspot.com/images/{user_id}/{filename}.jpg
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String url = FIREBASE_STORAGE_IMAGES_PATH + userId + "/" + fileName;
                callback.onFinishUploadFile(url);
            }
        });
    }

    public void getDownloadUrlFile(String name, final GetDownloadUrlFileStorageCallback callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(name);
        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    callback.onGetDownloadUrlFileSuccess(task.getResult());
                } else {
                    callback.onGetDownloadUrlFileFailure(task.getException());
                }
            }
        });
    }

    public void deleteFile(String name, final DeleteFileStorageCallback callback) {
        // Create a storage reference from our app
        StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(name);

        // Delete the file
        fileRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onDeleteFileSuccess();
                } else {
                    callback.onDeleteFileFailure(task.getException());
                }
            }
        });
    }

    private StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_STORAGE_PATH);
    }
}
