package com.jobayerjim.minecraftskins.ui.fragment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jobayerjim.minecraftskins.R;

import com.jobayerjim.minecraftskins.databinding.FragmentDetailsBinding;
import com.jobayerjim.minecraftskins.models.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import kotlin.io.FilesKt;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class DetailsFragment extends Fragment {
    FragmentDetailsBinding binding;
    String image;
    String fileName, resName, resFileName;
    String mainName, resMainName;
    Drawable imageDrawable;
    String imageName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mainFolder != null) {
            if (mainFolder.exists()) {
                FilesKt.deleteRecursively(mainFolder);
            }
        }
    }

    private FragmentManager fragmentManager;

    public DetailsFragment(String fileName, String resName, String imageName, FragmentManager fragmentManager) {
        this.fileName = fileName;
        this.resName = resName;
        this.imageName = imageName;
        this.fragmentManager = fragmentManager;
    }

    File rootFolder;
    InputStream ims = null;

    private void init() {
        rootFolder = new File(Environment.getExternalStorageDirectory(), Constants.MAIN_FOLDER);
        try {
            // get input stream
            mainName = imageName.replace(".png", "");
            resMainName = resName.replace(".png", "");
            resFileName = "res_skins/" + resName;
            Log.d("resPath", resFileName + " " + fileName);
            ims = requireContext().getAssets().open(fileName);
            // load image as Drawable
            imageDrawable = Drawable.createFromStream(ims, imageName);
            // set image to ImageView
            binding.contentImageDetails.setImageDrawable(imageDrawable);

        } catch(IOException ex) {
            ex.printStackTrace();
        }
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        binding.exportToGallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted() && ims!=null) {
                    copyAssets(fileName);
                }
            }
        });
        binding.exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    exportForMinecraft();
                }
            }
        });

    }

    private void killAppBypackage(String packageTokill) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = requireContext().getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);


        ActivityManager mActivityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        String myPackage = requireContext().getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {

            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            if (packageInfo.packageName.equals(myPackage)) {
                continue;
            }
            if (packageInfo.packageName.equals(packageTokill)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }

        }

    }

    private void createAllRequiredFile(File mainFolder) {
        JSONObject manifest = getManifestJson();
        if (manifest != null) {
            try {
                Writer output = null;
                File manifestFile = new File(mainFolder.getAbsolutePath(), Constants.MANIFEST);
                output = new BufferedWriter(new FileWriter(manifestFile));
                output.write(manifest.toString());
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject skins=getSkinJson(resMainName,resName);
        if (skins!=null) {
            try {
                Writer output = null;
                File skinFile = new File(mainFolder.getAbsolutePath(), Constants.SKIN_FILE);
                output = new BufferedWriter(new FileWriter(skinFile));
                output.write(skins.toString());
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        createPackIcon(mainFolder);
        createSkinFiles(mainFolder,resName);
        ArrayList<String> files=new ArrayList<>();
        for (File file: mainFolder.listFiles()) {
            files.add(file.getAbsolutePath());
        }
        zip(files,Environment.getExternalStorageDirectory()+Constants.MAIN_FOLDER+"/"+mainName+".zip",mainFolder);
    }
    File mainFolder=null;
    private void exportForMinecraft() {
        if (!rootFolder.exists()) {
            if (rootFolder.mkdir()) {
                mainFolder=new File(rootFolder,mainName);
            }
        }
        else {
            mainFolder=new File(rootFolder,mainName);;
        }
        if (mainFolder!=null) {
            if (mainFolder.exists()) {
                FilesKt.deleteRecursively(mainFolder);
            }
            String mcPackName="/"+mainName+".mcpack";
            File mcpack=new File(rootFolder,mcPackName);
            if (mcpack.exists()) {
                Log.d("mcPack", "found");
                openMcpackFile(mcpack);

            } else {
                if (mainFolder.mkdir()) {
                    Log.d("fileNotExist", "fileNotExist");
                    createAllRequiredFile(mainFolder);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.folder_failed), Toast.LENGTH_SHORT).show();
                }
                Log.d("filePath", mainFolder.getAbsolutePath());
            }

        }
        else {
            Toast.makeText(requireContext(), getString(R.string.folder_failed), Toast.LENGTH_SHORT).show();
        }


    }


    public void zip(ArrayList<String> files, String zipFileName,File main) {
        try {
            int BUFFER=6 * 1024;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < files.size(); i++) {
                Log.v("Compress", "Adding: " + files.get(i));
                FileInputStream fi = new FileInputStream(files.get(i));
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(files.get(i).substring(files.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
            File zipFile=new File(Environment.getExternalStorageDirectory()+Constants.MAIN_FOLDER,mainName+".zip");
            File mcpackFile=new File(Environment.getExternalStorageDirectory()+Constants.MAIN_FOLDER,mainName+".mcpack");
            if (zipFile.renameTo(mcpackFile))
            {
                FilesKt.deleteRecursively(main);
                openMcpackFile(mcpackFile);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMcpackFile(File mcpackFile) {

        Uri minecraftData = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", mcpackFile);
        // Uri minecraftData=Uri.parse("file:///storage/emulated/0/fromgate.mcpack");
        Intent intent = new Intent(Intent.ACTION_VIEW, minecraftData);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = requireContext().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 2);
        boolean isIntentSafe = activities.size() > 0;
// Start an activity if it's safe
        if (isIntentSafe) {
            killAppBypackage("com.mojang.minecraftpe");
            List<ResolveInfo> resInfoList = requireContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                requireContext().grantUriPermission(packageName, minecraftData, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            Toast.makeText(requireContext(), getString(R.string.minecraftImport), Toast.LENGTH_SHORT).show();
            startActivity(intent);
            fragmentManager.popBackStack();
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.not_found_minecraft), Toast.LENGTH_SHORT).show();
        }
    }

    private void createSkinFiles(File mainFolder, String name) {
        InputStream is;
        OutputStream out = null;
        try {
            is = requireContext().getAssets().open(resFileName);
            File outFile = new File(mainFolder.getAbsolutePath(), name);
            out = new FileOutputStream(outFile);
            //copyFile(ims, out);
            byte[] data = new byte[is.available()];
            is.read(data);
            out.write(data);
            is.close();
            out.close();
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + name, e);
        }
    }

    private void createPackIcon(File mainFolder) {
        try {
            InputStream is = requireContext().getAssets().open(fileName);
            Bitmap logo = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 128, 128, true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            logo.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            File outFile = new File(mainFolder.getAbsolutePath(), Constants.PACK_ICON);
            OutputStream out = new FileOutputStream(outFile);
            //copyFile(ims, out);
            byte[] data = new byte[bs.available()];
            bs.read(data);
            out.write(data);
            bs.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getManifestJson() {
        JSONObject manifest=new JSONObject();
        try {

            manifest.accumulate("format_version",1);
            JSONObject header=new JSONObject();
            header.accumulate("name",mainName);
            header.accumulate("uuid", UUID.randomUUID().toString());
            JSONArray version=new JSONArray();
            version.put(1);
            version.put(0);
            version.put(0);
            header.accumulate("version",version);
            manifest.accumulate("header",header);
            JSONArray modules =new JSONArray();
            JSONObject module=new JSONObject();
            module.accumulate("type","skin_pack");
            module.accumulate("uuid",UUID.randomUUID().toString());
            module.accumulate("version",version);
            modules.put(module);
            manifest.accumulate("modules",modules);
            Log.d("manifest.json",manifest.toString());
            return manifest;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
    private JSONObject getSkinJson(String name,String fileName) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("geometry", "skinpacks/skins.json");
            jsonObject.accumulate("localization_name", mainName);
            jsonObject.accumulate("serialize_name", mainName);
            JSONArray skins = new JSONArray();
            JSONObject object1 = new JSONObject();
            object1.accumulate("geometry", "geometry.humanoid.custom");
            object1.accumulate("localization_name", "current");
            object1.accumulate("texture", fileName);
            object1.accumulate("type", "free");
            skins.put(object1);
            jsonObject.put("skins",skins);
            Log.d("skinString",jsonObject.toString());
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    private void copyAssets(String fName) {
        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.exporting));
        progressDialog.show();
            InputStream is;
            OutputStream out = null;
            try {
                is = requireContext().getAssets().open(fName);
                File outFile = new File(Environment.getExternalStorageDirectory()+"/Download/", imageName);
                out = new FileOutputStream(outFile);
                //copyFile(ims, out);
                byte[] data = new byte[is.available()];
                is.read(data);
                out.write(data);
                is.close();
                out.close();
                MediaScannerConnection.scanFile(requireContext(),
                        new String[]{outFile.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                Toast.makeText(requireContext(), getResources().getString(R.string.export_success), Toast.LENGTH_SHORT).show();
                Log.d("fileName", fName + " " + imageName + " " + outFile.getAbsolutePath());
                progressDialog.dismiss();
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + fName, e);
                progressDialog.dismiss();
            }

    }
    final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("DetailsActivity", "Permission is granted");
                return true;
            } else {

                Log.v("DetailsActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("DetailsActivity","Permission is granted");
            return true;
        }
    }
}