package com.cuna.firebaselogin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import modelosdatos.Model;

public class MyRecycleViewHolder extends RecyclerView.Adapter<MyRecycleViewHolder.ViewHolder> implements View.OnClickListener {



    /*estructura de datos para llenar los elementos graficos*/
    private ArrayList<Model> modelList;


    //agregamos un listener
    private View.OnClickListener listener;


    /*crear un constructr para inicializar la lista de modelos,con los datos
     que manda firebase y poder usarlos*/
    public MyRecycleViewHolder(ArrayList<Model>modelList){

        this.modelList=modelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*asignar xml*/
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item_db,parent
                ,false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model model=modelList.get(position);

        holder.lblID.setText(model.getId());
        holder.lblGroup.setText(model.getGroup());
        holder.lblActivity.setText(model.getActivity());
        holder.lbLecture.setText(model.getLecture());


    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;

    }

    @Override
    public void onClick(View view) {

        if(listener!=null){

            listener.onClick(view);

        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        /*aqui vamos a inicializar los componentes graficos del xml con el texto que trae la
        lista de objetos que manda el usuario al acceder a la base de datos de firebase*/

        private TextView lblID,lblGroup,lblActivity, lbLecture;
        public View view;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            this.view=itemView;
            this.lblID=view.findViewById(R.id.lblIDModel);
            this.lblActivity=view.findViewById(R.id.lblActivityModel);
            this.lblGroup=view.findViewById(R.id.lblGroupModel);
            this.lbLecture=view.findViewById(R.id.lblLectureModel);

        }

    }

}
