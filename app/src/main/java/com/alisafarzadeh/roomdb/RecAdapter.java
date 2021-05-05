package com.alisafarzadeh.roomdb;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisafarzadeh.roomdb.Room.MainData;
import com.alisafarzadeh.roomdb.Room.RoomHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.Holder> implements TextToSpeech.OnInitListener {

    List<MainData> dataList=new ArrayList<>();
    RoomHolder dbholder;
    Context context;
    TextToSpeech tts;
    List<String> persian_select=new ArrayList<>();
    List<String>  english_select=new ArrayList<>();

    public RecAdapter(Context context,  List<MainData> data) {
        this.context = context;
        this.dataList = data;

        tts = new TextToSpeech(context,this);
        notifyDataSetChanged();


    }

    public List<String> getPersian_select() {
        return persian_select;
    }

    public List<String> getEnglish_select() {
        return english_select;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new Holder(view);
    }
    MainData data;
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        data = dataList.get(position);
        dbholder = RoomHolder.getInstance(context);
        holder.per.setText(data.getPersian());
        holder.eng.setText(data.getEnglish());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked==true)
                {
                    persian_select.add(dataList.get(position).getPersian());
                    english_select.add(dataList.get(position).getEnglish());
                    String temp = "";
                    for (int i = 0; i < english_select.size(); i++) {
                        temp+=english_select.get(i)+":"+persian_select.get(i)+"\n";
                    }
                    Toast.makeText(context, temp + "", Toast.LENGTH_SHORT).show();

                }else {

                    persian_select.remove(dataList.get(position).getPersian());
                    english_select.remove(dataList.get(position).getEnglish());
                    String temp = "";
                    for (int i = 0; i < english_select.size(); i++) {
                        temp+=english_select.get(i)+":"+persian_select.get(i)+"\n";
                    }
                    Toast.makeText(context, temp + "", Toast.LENGTH_SHORT).show();

                }


            }
        });
        holder.visable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.per.getVisibility()==View.INVISIBLE) {
                    holder.per.setVisibility(View.VISIBLE);
                    holder.visable.setImageResource(R.drawable.ic_baseline_visibility_24);
                }else {
                    holder.visable.setImageResource(R.drawable.visibility_off);
                    holder.per.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(dataList.get(position).getEnglish(), TextToSpeech.QUEUE_ADD, null,dataList.get(position).getEnglish());
                }
                else {
                    tts.speak(dataList.get(position).getEnglish(), TextToSpeech.QUEUE_ADD,null);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                MainData data1 = dataList.get(holder.getAdapterPosition());
                try {
                    dbholder.mainDao().delete(data1.getUid());
                }catch (Exception e)
                {
                    Toast.makeText(context, e.getCause()+"", Toast.LENGTH_SHORT).show();
                }
                 */

                /*
                new RoomHolder.deleteAllWordsAsyncTask(dbholder.mainDao()).execute();
                 */

                //new RoomHolder.deleteAllWordsAsyncTask(dbholder.mainDao(),dataList.get(holder.getAdapterPosition())).execute();

                for (int i = 0; i < dbholder.mainDao().getAllData().size() ; i++) {

                    Log.d("xxxHolder",dbholder.mainDao().getAllData().get(i).getUid()+"");
                }
                Log.d("xxxHolder","::   "+dbholder.mainDao().getAllData().get(position).getUid());


                MainData mainData = new MainData();
                //mainData.setUid(dataList.get(holder.getAdapterPosition()).getUid());
                mainData.setUid(dbholder.mainDao().getAllData().get(position).getUid());
                new RoomHolder.deleteAllWordsAsyncTask(dbholder.mainDao(),mainData).execute();
                //dbholder.mainDao().delete(mainData);



                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.US);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView per,eng;
        ImageView delete,speech,visable;
        CheckBox checkBox;
        public Holder(@NonNull View itemView) {
            super(itemView);
            per = itemView.findViewById(R.id.TextPer);
            eng = itemView.findViewById(R.id.TextEng);
            delete = itemView.findViewById(R.id.deleteitem);
            speech = itemView.findViewById(R.id.speechitem);
            visable = itemView.findViewById(R.id.visableitem);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
