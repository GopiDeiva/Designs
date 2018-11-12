package hatchure.designs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hatchure.designs.Adapter.OfferTypeAdapter;
import hatchure.designs.Helpers.Utils;
import hatchure.designs.Interfaces.ApiInterface;
import hatchure.designs.Interfaces.ICustomClickEvent;
import hatchure.designs.Models.AdTypeList;
import hatchure.designs.Models.OfferTitle;
import hatchure.designs.Models.OfferTitleList;
import hatchure.designs.Models.ShopCategory;
import hatchure.designs.Models.ShopCategoryList;
import hatchure.designs.WebHandler.WebRequesthandler;
import retrofit2.Call;
import retrofit2.Callback;

public class InStoreAds extends Fragment implements ICustomClickEvent {

    int adsTypeId, offerTypeId, shopCategoryId, listPostion;
    String offerDescriptionValue, fromDurationValue, toDurationValue, offerTitleValue, imagePath;
    TextView offerTypeSelector, category, description, fromDuration, toDuration;
    EditText offerTitle;
    ImageView productImage;
    Button fromDurationSelect, toDurationSelect;
    List<OfferTitle> offerTitleCollection;
    List<ShopCategory> shopCategoryCollection;
    DateFormat dateFormat;
    Dialog dialog;

    enum ListTypes {OFFER, CATEGORY}

    ;
    ListTypes selectedListType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instore_postads_fragment, container, false);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        offerTypeSelector = view.findViewById(R.id.offerTypeValue);
        category = view.findViewById(R.id.offerCategoryValue);
        offerTitle = view.findViewById(R.id.offerTitleValue);
        description = view.findViewById(R.id.offerDescription);
        productImage = view.findViewById(R.id.productImage);
        fromDuration = view.findViewById(R.id.offerPeriodFromValue);
        toDuration = view.findViewById(R.id.offerPeriodToValue);
        fromDurationSelect = view.findViewById(R.id.offerPeriodFromButton);
        toDurationSelect = view.findViewById(R.id.offerPeriodToButton);

        Button preview = view.findViewById(R.id.preview);
        Button submit = view.findViewById(R.id.submit);

        if (Utils.IsNetworkAvailable(getContext())) {
            //offerTitleCollection = new ArrayList();
            //shopCategoryCollection= new ArrayList();
            SetAdsId();

            offerTitleCollection = PrepareOfferTypes();
            shopCategoryCollection = PrepareCategoryList();
//            if (offerTitleCollection != null && offerTitleCollection.size() > 0) {
//                SetOfferType(0);
//            }
//            if (shopCategoryCollection != null && shopCategoryCollection.size() > 0) {
//                SetcategoryType(0);
//            }

            Calendar calendar = Calendar.getInstance();
            Date currentDate = new Date();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, 1);
            fromDurationValue = dateFormat.format(currentDate);
            toDurationValue = dateFormat.format(calendar.getTime());
            fromDuration.setText(fromDurationValue);
            toDuration.setText(toDurationValue);
        }


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUIValues();
                if (ValidateFields()) {

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUIValues();
                if (ValidateFields()) {

                }
            }
        });


        offerTypeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedListType = ListTypes.OFFER;
                if (offerTitleCollection == null) {
                    offerTitleCollection = PrepareOfferTypes();
                }
                ArrayList offerList = new ArrayList();
                if (offerTitleCollection != null) {
                    for (OfferTitle title : offerTitleCollection) {
                        offerList.add(title.getOfferTitleName());
                    }
                    ShowListAlertBox(offerList, "Offer types");
                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedListType = ListTypes.CATEGORY;
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

        fromDurationSelect.setOnClickListener(new View.OnClickListener() {
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


        return view;
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

    private void ShowTimePicker(final String date, final boolean isFromDuration){
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
                            val =dateFormat.format(dateFormat.parse(date+ " "+hourOfDay+":"+minute+":00"));


                        } catch (ParseException e) {
                            Toast.makeText(getContext(),"failiure"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        if(isFromDuration)
                        {
                            fromDurationValue = val;
                            fromDuration.setText(fromDurationValue);
                        }
                        else
                        {
                            toDurationValue = val;
                            toDuration.setText(toDurationValue);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
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

    private List<OfferTitle> PrepareOfferTypes() {
        final List<OfferTitle> titles = new ArrayList<>();
        final ProgressDialog p = Utils.GetProcessDialog(getContext());
        p.show();
        ApiInterface apiService =
                WebRequesthandler.getClient().create(ApiInterface.class);

        Call<OfferTitleList> call = apiService.GetOfferTypes();
        call.request();
        call.enqueue(new Callback<OfferTitleList>() {
            @Override
            public void onResponse(Call<OfferTitleList> call, retrofit2.Response<OfferTitleList> response) {
                Log.d("offer types", response.body().toString());
                p.dismiss();
                titles.addAll(response.body().getOfferTitlesList());
            }

            @Override
            public void onFailure(Call<OfferTitleList> call, Throwable t) {
                p.dismiss();
                Toast.makeText(getContext(), "failiure", Toast.LENGTH_SHORT).show();
            }
        });
        return titles;
    }

    private void GetUIValues() {
        offerTitleValue = offerTitle.getText().toString().trim();
        offerDescriptionValue = description.getText().toString().trim();
    }

    private boolean ValidateFields() {
        if (offerTitleValue.equals("")||offerTitleValue.isEmpty()) {
            Toast.makeText(getContext(), "Offer title should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (offerTitleCollection == null && shopCategoryCollection == null) {
            Toast.makeText(getContext(), "Please select offer type and shop category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (offerTitleCollection == null && shopCategoryCollection != null) {
            Toast.makeText(getContext(), "Please select offer type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (offerTitleCollection != null && shopCategoryCollection == null) {
            Toast.makeText(getContext(), "Please select shop category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (offerDescriptionValue.isEmpty()) {
            Toast.makeText(getContext(), "Offer description should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                adsTypeId = Integer.parseInt(response.body().getAdTypes().get(0).getAdTypeId());
            }

            @Override
            public void onFailure(Call<AdTypeList> call, Throwable t) {
                p.dismiss();
            }
        });
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

    @Override
    public void OnCustomClick(int position) {
        if (selectedListType.equals(ListTypes.OFFER)) {
            SetOfferType(position);
        } else {
            SetcategoryType(position);
        }

        if (dialog != null)
            dialog.dismiss();
    }

    private void SetOfferType(int position) {
        if (offerTitleCollection != null && offerTitleCollection.size() > 0) {
            offerTypeSelector.setText(offerTitleCollection.get(position).getOfferTitleName());
            offerTypeId = Integer.parseInt(offerTitleCollection.get(position).getOfferTitleId());
        }
    }

    private void SetcategoryType(int position) {
        if (shopCategoryCollection != null && shopCategoryCollection.size() > 0) {
            category.setText(shopCategoryCollection.get(position).getCategoryName());
            shopCategoryId = Integer.parseInt(shopCategoryCollection.get(position).getCategoryId());
        }
    }
}