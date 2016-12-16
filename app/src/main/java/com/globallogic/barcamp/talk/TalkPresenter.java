package com.globallogic.barcamp.talk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.globallogic.barcamp.data.firebase.callback.DeleteFileStorageCallback;
import com.globallogic.barcamp.data.firebase.callback.FirebaseTalkDataCallback;
import com.globallogic.barcamp.data.firebase.callback.UploadFileStorageCallback;
import com.globallogic.barcamp.domain.Room;
import com.globallogic.barcamp.domain.Talk;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public class TalkPresenter extends BasePresenter<TalkView> implements AdapterView.OnItemSelectedListener {

    private static final String BARCAMP_HOME = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Barcamp/";

    private static final int TAKE_PHOTO_CODE = 0;
    private static final int PICK_PICTURE_CODE = 1;

    private TalkMode talkMode;
    private String talkId;
    private Talk currentTalk;
    private boolean wasChanges = false;

    private File picture;
    private boolean fromCamera = false;
    private FirebaseWorker firebaseWorker = new FirebaseWorker();

    public enum TalkMode {
        ADD, EDIT
    }

    private FirebaseTalkDataCallback<Talk> dataCallback = new FirebaseTalkDataCallback<Talk>() {
        @Override
        public void onDataChanged(Talk data) {
            currentTalk = data;
            view.roomRender(getAvailableRooms(currentTalk.getRoomName()));
            view.renderFields(data);
            view.addTextWatchers();
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Do nothing for now..
        }
    };

    public TalkPresenter(TalkView view, TalkMode talkMode, String talkId) {
        super(view);
        this.talkMode = talkMode;
        this.talkId = talkId;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showBackButton();
        view.setTitle();
        view.setSubmitButtonText();

        // check for camera feature
        checkCameraFeature();

        if (talkMode.equals(TalkMode.EDIT)) {
            firebaseWorker.addTalkEventListener(talkId, dataCallback);
        } else {
            view.roomRender(getAvailableRooms(view.getTalkRoomSelected()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.addTextWatchers();
                }
            }, 300);
        }
    }

    @Override
    public void onDestroyView() {
        removeTalkItemEventListener();
    }

    public long getTalkTime() {
        return view.getTalkTime();
    }

    public void onSubmitButtonPressed(final Talk talk) {
        // validate fields
        boolean isValid = true;
        if ("".equals(talk.getName())) {
            isValid = false;
            view.showErrorEmptyName();
        }
        if ("".equals(talk.getSpeakerName())) {
            isValid = false;
            view.showErrorEmptySpeaker();
        }

        if (isValid) {
            wasChanges = false;
            view.showSavingDialog();
            if (talkMode.equals(TalkMode.ADD)) {
                // adding talk
                addTalk(talk);
            } else {
                // editing talk
                updateTalk(talk);
            }
        }
    }

    @Override
    public boolean canBack() {
        if (wasChanges) {
            view.showLostChangesConfirmation(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // allow to back
                    wasChanges = false;
                    removeTalkItemEventListener();
                    view.onBackPressed();
                }
            });
            return false;
        }
        return true;
    }

    public void checkChanges(Talk talk) {
        if (!wasChanges && !isEmpty(talk)) {
            if (talkMode.equals(TalkMode.ADD)) {
                wasChanges = true;
            } else {
                wasChanges = !talk.getName().equals(currentTalk.getName()) ||
                        !talk.getSpeakerName().equals(currentTalk.getSpeakerName()) ||
                        !talk.getDescription().equals(currentTalk.getDescription()) ||
                        !talk.getTwitter().equals(currentTalk.getTwitter());
            }
        }
    }

    private void uploadImage(final Talk talk) {
        // execute upload image
        firebaseWorker.uploadStorageItem(resolvePicturePath(), new UploadFileStorageCallback() {

            @Override
            public void onFailureUploadFile(Throwable error) {
                Log.e(TAG, "Error uploading file", error);
                closeScreen();
            }

            @Override
            public void onFinishUploadFile(String urlFile) {
                // update photo uri
                talk.setPhoto(urlFile);
                if (talkMode.equals(TalkMode.EDIT)) {
                    firebaseWorker.update(talk);
                } else {
                    firebaseWorker.add(talk);
                }
                closeScreen();
            }
        });
    }

    private void removeTalkItemEventListener() {
        if (talkMode == TalkMode.EDIT) {
            firebaseWorker.removeTalkItemEventListener(talkId);
        }
    }

    private boolean isEmpty(Talk talk) {
        return talk.getName().isEmpty() &&
                talk.getSpeakerName().isEmpty() &&
                talk.getDescription().isEmpty() &&
                talk.getTwitter().isEmpty();
    }

    private void closeScreen() {
        view.dismissSavingDialog();

        // close keyboard if it is open
        view.closeKeyboard();

        // return to talk list
        view.onBackPressed();
    }

    private void addTalk(final Talk talk) {
        BarcampApplication.get().addTakenRoom(talk.getDate(), new Room(talk.getRoomName()));
        if (resolvePicturePath() != null) {
            uploadImage(talk);
        } else {
            firebaseWorker.add(talk);
            closeScreen();
        }
    }

    private void updateTalk(final Talk talk) {
        removeTalkItemEventListener();
        if (resolvePicturePath() != null) {
            uploadImage(talk);
        } else {
            updatePhotoPath(talk);
            firebaseWorker.update(talk);
            closeScreen();
        }
    }

    private void updatePhotoPath(Talk talk) {
        if (currentTalk.getPhoto() != null && !currentTalk.getPhoto().isEmpty()) {
            talk.setPhoto(currentTalk.getPhoto());
        }
    }

    private String resolvePicturePath() {
        if (picture != null) {
            return picture.getAbsolutePath();
        }
        return null;
    }

    public boolean hasPhoto(String photo) {
        return photo != null && !"".equals(photo) && containsImageMap(photo);
    }

    private boolean containsImageMap(String photo) {
        return BarcampApplication.get().containsUrl(photo);
    }

    public void onPhotoButtonPressed() {
        fromCamera = false;
        Intent getIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent.setType("image/*");
        view.startActivityResult(getIntent, PICK_PICTURE_CODE);
    }

    public void onCameraButtonPressed() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String barcampDir = BARCAMP_HOME;
        File newdir = new File(barcampDir);
        if (!newdir.exists()) {
            newdir.mkdirs();
        }

        // create the file name
        String fileName = barcampDir + "android-" + System.currentTimeMillis() + ".jpg";
        picture = new File(fileName);
        try {
            Log.i(TAG, "Start to create a new file in " + picture.getAbsolutePath());
            Log.i(TAG, "New file created: " + picture.createNewFile());
        } catch (IOException e) {
            Log.e(TAG, "Error creating new file", e);
        }

        // put the file uri to intent
        Uri outputFileUri = Uri.fromFile(picture);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        if (cameraIntent.resolveActivity(BarcampApplication.get().getPackageManager()) != null) {
            fromCamera = true;
            view.startActivityResult(cameraIntent, TAKE_PHOTO_CODE);
        } else {
            // could not resolved
            onClearPicturePressed();
        }
    }

    public void onClearPicturePressed() {
        view.showDeleteImageConfirmation(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // user confirmed
                dialogInterface.dismiss();
                if (talkMode.equals(TalkMode.EDIT)) {
                    if (resolvePicturePath() != null) {
                        Log.d(TAG, "Picture deleted locally = " + picture.delete());
                        fromCamera = false;
                        view.showPictureButtons();
                        checkCameraFeature();
                    } else if (hasPhoto(currentTalk.getPhoto())) {
                        // delete remotely
                        firebaseWorker.deleteStorageItem(currentTalk.getPhoto(), new DeleteFileStorageCallback() {
                            @Override
                            public void onDeleteFileSuccess() {
                                currentTalk.setPhoto(null);
                                picture = null;
                                firebaseWorker.update(currentTalk);
                                view.showPictureButtons();
                                checkCameraFeature();
                            }

                            @Override
                            public void onDeleteFileFailure(Throwable error) {
                                view.showDeleteFailure();
                            }
                        });
                    }
                } else {
                    if (fromCamera) {
                        Log.d(TAG, "Picture deleted locally = " + picture.delete());
                        fromCamera = false;
                    }
                    picture = null;
                    view.showPictureButtons();
                    checkCameraFeature();
                }
            }
        });
    }

    // begin RoomSpinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String newRoom = getAvailableRooms(this.view.getTalkRoomSelected()).get(position);
        if (!wasChanges) {
            if (this.currentTalk == null) {
                wasChanges = true;
            } else {
                wasChanges = !this.currentTalk.getRoomName().equals(newRoom);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Nothing to do..
    }
    // end RoomSpinner

    public List<String> getAvailableRooms(String roomNameSelected) {
        List<Room> rooms = BarcampApplication.get().getConfigData().getRooms();
        List<String> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            boolean isTaken = BarcampApplication.get().isTakenRoom(getTalkTime(), room);
            if (room.getName().equals(roomNameSelected) || !isTaken) {
                availableRooms.add(room.getName());
            }
        }
        return availableRooms;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                wasChanges = true;
                Log.d(TAG, "Picture saved");
                view.showPicture(picture.getAbsolutePath(), computeSize());
            } else {
                // otherwise, delete the picture
                onClearPicturePressed();
            }
        } else if (requestCode == PICK_PICTURE_CODE && resultCode == RESULT_OK && data != null) {
            wasChanges = true;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = BarcampApplication.get().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            picture = new File(picturePath);

            view.showPicture(picture.getAbsolutePath(), computeSize());
        }
    }

    /**
     * Computes the bitmap size
     * @return Options the computed bitmap size
     */
    private BitmapFactory.Options computeSize() {
        // Get the dimensions of the View
        int targetW = (int) BarcampApplication.get().getResources().getDimension(R.dimen.picture_dimen);
        int targetH = (int) BarcampApplication.get().getResources().getDimension(R.dimen.picture_dimen);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picture.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return bmOptions;
    }

    private void checkCameraFeature() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            view.hideCameraButton();
        }
    }

    private PackageManager getPackageManager() {
        return BarcampApplication.get().getPackageManager();
    }
}
