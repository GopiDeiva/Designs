package hatchure.designs;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hatchure.designs.Adapter.OfferTypeAdapter;
import hatchure.designs.Adapter.ProductImagesSliderAdapter;
import hatchure.designs.Helpers.Utils;
import hatchure.designs.Interfaces.ApiInterface;
import hatchure.designs.Interfaces.ICustomClickEvent;
import hatchure.designs.Models.AdTypeList;
import hatchure.designs.Models.ShopCategory;
import hatchure.designs.Models.ShopCategoryList;
import hatchure.designs.WebHandler.WebRequesthandler;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;

public class ProductAds extends Fragment implements ICustomClickEvent {
    ArrayList<Uri> imageUris;
    List<ShopCategory> shopCategoryCollection;
    int adsTypeId, shopCategoryId;
    String offerDescriptionValue, fromDurationValue, toDurationValue, titleValue, productName, originalPrice, discountPrice, discountPercentage;
    TextView category, description, fromDuration, toDuration, addImages;
    DateFormat dateFormat;
    EditText offerTitle, originalPriceValue, discountValue, productNameValue, discountPercentageValue;
    ViewPager view_pager;
    ImageView imageView;
    Button fromDurationSelect, toDurationSelect;
    Dialog dialog;
    DiscountType discountType;
    enum DiscountType{by_price , by_percentage}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_postads_fragment, container, false);
        //TODO: get them from intent
        final String shopId = "1001";
        final String userId = "1002";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        productNameValue = view.findViewById(R.id.productNameValue);
        category = view.findViewById(R.id.offerCategoryValue);
        offerTitle = view.findViewById(R.id.offerTitleValue);
        description = view.findViewById(R.id.offerDescriptionValue);
        fromDuration = view.findViewById(R.id.offerPeriodFromValue);
        toDuration = view.findViewById(R.id.offerPeriodToValue);
        fromDurationSelect = view.findViewById(R.id.offerPeriodFromButton);
        toDurationSelect = view.findViewById(R.id.offerPeriodToButton);
        addImages = view.findViewById(R.id.addImages);
        view_pager = view.findViewById(R.id.view_pager);
        originalPriceValue = view.findViewById(R.id.originalPriceValue);
        discountValue = view.findViewById(R.id.discountValue);
        discountPercentageValue = view.findViewById(R.id.discountPercentageValue);
        imageView = view.findViewById(R.id.imageView);
        Button preview = view.findViewById(R.id.preview);
        Button submit = view.findViewById(R.id.submit);
        imageView.setVisibility(View.VISIBLE);
        if (Utils.IsNetworkAvailable(getContext())) {
            SetAdsId();
            shopCategoryCollection = PrepareCategoryList();
            SetDefauldDuration();
        }

        discountValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    discountPercentageValue.setText("");
                    discountType = DiscountType.by_price;
                }
            }
        });

        discountPercentageValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    discountValue.setText("");
                    discountType = DiscountType.by_percentage;
                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUIValues();
                if (ValidateFields()) {
                    Toast.makeText(getContext(), "fields validated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUIValues();
                if (ValidateFields()) {
                    RequestBody adsTypeBody = adsTypeId == 0 ? RequestBody.create(MediaType.parse("text/plain"), String.valueOf(48))//instore ads type id
                            : RequestBody.create(MediaType.parse("text/plain"), String.valueOf(adsTypeId));
                    RequestBody descriptionBody = null;
                    if (!offerDescriptionValue.isEmpty())
                        descriptionBody = RequestBody.create(MediaType.parse("text/plain"), titleValue);

                    if(imageUris!=null && imageUris.size()>0){
                    MultipartBody.Part[] imagesBody = new MultipartBody.Part[imageUris.size()];
                        for (int i=0;i<imageUris.size();i++) {
                            String filePath = Utils.GetRealFilePathFromURIPath(getContext(), imageUris.get(i));
                            File file = new File(filePath);
                            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                            imagesBody[i] = MultipartBody.Part.createFormData("file"+i, file.getName(), mFile);
                        }

                        final ProgressDialog p = Utils.GetProcessDialog(getContext());
                        p.show();
                        ApiInterface apiService =
                                WebRequesthandler.getClient().create(ApiInterface.class);

                        Call<Object> call;
                        if(descriptionBody!=null) {
                            call = apiService.PostProductAds(adsTypeBody,
                                    RequestBody.create(MediaType.parse("text/plain"), userId),
                                    RequestBody.create(MediaType.parse("text/plain"), shopId),
                                    RequestBody.create(MediaType.parse("text/plain"), productName),
                                    RequestBody.create(MediaType.parse("text/plain"), titleValue),
                                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(offerTypeId)),//TODO
                                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(shopCategoryId)),
                                    RequestBody.create(MediaType.parse("text/plain"), discountType.toString()),
                                    RequestBody.create(MediaType.parse("text/plain"), discountPrice),
                                    RequestBody.create(MediaType.parse("text/plain"), discountPercentage),
                                    RequestBody.create(MediaType.parse("text/plain"), fromDurationValue),
                                    RequestBody.create(MediaType.parse("text/plain"), toDurationValue),
                                    descriptionBody,
                                    imagesBody
                            );
                        }
                        else {
                            call = apiService.PostProductAds(adsTypeBody,
                                    RequestBody.create(MediaType.parse("text/plain"), userId),
                                    RequestBody.create(MediaType.parse("text/plain"), shopId),
                                    RequestBody.create(MediaType.parse("text/plain"), productName),
                                    RequestBody.create(MediaType.parse("text/plain"), titleValue),
                                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(offerTypeId)),//TODO
                                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(shopCategoryId)),
                                    RequestBody.create(MediaType.parse("text/plain"), discountType.toString()),
                                    RequestBody.create(MediaType.parse("text/plain"), discountPrice),
                                    RequestBody.create(MediaType.parse("text/plain"), discountPercentage),
                                    RequestBody.create(MediaType.parse("text/plain"), fromDurationValue),
                                    RequestBody.create(MediaType.parse("text/plain"), toDurationValue),
                                    imagesBody
                            );
                        }
                        call.request();
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                                Log.d("offer types", response.body().toString());
                                p.dismiss();
                                Toast.makeText(getContext(), "posted successfully = " + response.body().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                p.dismiss();
                                Toast.makeText(getContext(), "failiure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(),"Product images are not available in memory", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopCategoryCollection == null) {
                    shopCategoryCollection = PrepareCategoryList();
                }
                ArrayList categoryList = new ArrayList();
                if (shopCategoryCollection != null) {
                    for (ShopCategory category : shopCategoryCollection) {
                        categoryList.add(category.getCategoryName());
                    }
                    ShowListAlertBox(categoryList, "Categories");
                }
            }
        });

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            123);
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        fromDurationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateTime(true);
            }
        });

        fromDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateTime(true);
            }
        });

        toDurationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateTime(false);
            }
        });

        toDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateTime(false);
            }
        });


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if (imageUris != null) {
                        imageUris.clear();
                    } else {
                        imageUris = new ArrayList<>();
                    }
                    if (data.getData() != null) {
                        imageUris.add(data.getData());
                    } else if (data.getClipData() != null) {
                        ClipData val = data.getClipData();
                        if(val.getItemCount()>4)
                            Toast.makeText(getContext(), "Select only 4 images", Toast.LENGTH_SHORT).show();
                        else {
                            for (int i = 0; i < val.getItemCount(); i++) {
                                imageUris.add(val.getItemAt(i).getUri());
                            }
                        }
                    }
                    SetProductImage();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetProductImage() {
        imageView.setVisibility(View.GONE);
        ProductImagesSliderAdapter adapter = new ProductImagesSliderAdapter(getContext(), imageUris);
        view_pager.setAdapter(adapter);
    }

    private void GetUIValues() {
        productName = productNameValue.getText().toString().trim();
        titleValue = offerTitle.getText().toString().trim();
        offerDescriptionValue = description.getText().toString().trim();
        originalPrice = originalPriceValue.getText().toString().trim();
        discountPrice = discountValue.getText().toString().trim();
        discountPercentage = discountPercentageValue.getText().toString().trim();
    }

    private boolean ValidateFields() {
        if (productName == null || productName.isEmpty()) {
            Toast.makeText(getContext(), "Product name should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (titleValue == null || titleValue.isEmpty()) {
            Toast.makeText(getContext(), "Offer title should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (shopCategoryCollection == null || category.getText().toString() == null || category.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please select shop category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageUris == null) {
            Toast.makeText(getContext(), "Please select product images", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageUris != null) {
            if (!(imageUris.size() > 0)) {
                Toast.makeText(getContext(), "Please select at least one product image", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (imageUris.size() > 4) {
                Toast.makeText(getContext(), "Maximum only four images are allowed", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (originalPrice == null || originalPrice.isEmpty()) {
            Toast.makeText(getContext(), "Original price should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (((discountPrice == null || discountPrice.isEmpty()) && (discountPercentage == null || discountPercentage.isEmpty()))
                || ((discountPrice != null && !discountPrice.isEmpty()) && (discountPercentage != null && !discountPercentage.isEmpty()))) {
            Toast.makeText(getContext(), "Please enter either discount price or percentage", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (offerDescriptionValue == null || offerDescriptionValue.isEmpty()) {
            Toast.makeText(getContext(), "Offer description should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void ShowListAlertBox(ArrayList list, String title) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_list_dialog_box);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView listTitle = dialog.findViewById(R.id.listTitle);
        listTitle.setText(title);
        Button cancel = dialog.findViewById(R.id.cancel);
        final RecyclerView mRecyclerView = dialog.findViewById(R.id.recyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        OfferTypeAdapter myAdapter = new OfferTypeAdapter(this, list);
        mRecyclerView.setAdapter(myAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void SetDateTime(final boolean isFromDuration) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ShowTimePicker(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, isFromDuration);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void ShowTimePicker(final String date, final boolean isFromDuration) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String val = null;
                        try {
                            val = dateFormat.format(dateFormat.parse(date + " " + hourOfDay + ":" + minute + ":00"));


                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "failiure" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (isFromDuration) {
                            fromDurationValue = val;
                            fromDuration.setText(fromDurationValue);
                        } else {
                            toDurationValue = val;
                            toDuration.setText(toDurationValue);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void SetDefauldDuration() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 1);
        fromDurationValue = dateFormat.format(currentDate);
        toDurationValue = dateFormat.format(calendar.getTime());
        fromDuration.setText(fromDurationValue);
        toDuration.setText(toDurationValue);
    }

    private List<ShopCategory> PrepareCategoryList() {
        final List<ShopCategory> categories = new ArrayList<>();
        final ProgressDialog p = Utils.GetProcessDialog(getContext());
        p.show();
        ApiInterface apiService =
                WebRequesthandler.getClient().create(ApiInterface.class);

        Call<ShopCategoryList> call = apiService.GetShopCategories();
        call.request();
        call.enqueue(new Callback<ShopCategoryList>() {
            @Override
            public void onResponse(Call<ShopCategoryList> call, retrofit2.Response<ShopCategoryList> response) {
                Log.d("cstegories", response.body().toString());
                p.dismiss();
                categories.addAll(response.body().getShopCategoriesList());
            }

            @Override
            public void onFailure(Call<ShopCategoryList> call, Throwable t) {
                p.dismiss();
            }
        });
        return categories;
    }

    private void SetAdsId() {
        final ProgressDialog p = Utils.GetProcessDialog(getContext());
        p.show();
        ApiInterface apiService =
                WebRequesthandler.getClient().create(ApiInterface.class);

        Call<AdTypeList> call = apiService.GetAdTypes();
        call.request();
        call.enqueue(new Callback<AdTypeList>() {
            @Override
            public void onResponse(Call<AdTypeList> call, retrofit2.Response<AdTypeList> response) {
                Log.d("Ads type", response.body().toString());
                p.dismiss();
                adsTypeId = Integer.parseInt(response.body().getAdTypes().get(1).getAdTypeId());
            }

            @Override
            public void onFailure(Call<AdTypeList> call, Throwable t) {
                p.dismiss();
            }
        });
    }

    @Override
    public void OnCustomClick(int position) {
            SetcategoryType(position);

        if (dialog != null)
            dialog.dismiss();
    }

    private void SetcategoryType(int position) {
        if (shopCategoryCollection != null && shopCategoryCollection.size() > 0) {
            category.setText(shopCategoryCollection.get(position).getCategoryName());
            shopCategoryId = Integer.parseInt(shopCategoryCollection.get(position).getCategoryId());
        }
    }
}