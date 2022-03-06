package com.example.androidproject.activities.mainactivity.viewholders;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.activities.mainactivity.ClassSelected;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.example.androidproject.R;

import org.jetbrains.annotations.NotNull;

public class ClassiViewHolder extends RecyclerView.ViewHolder{

    TextView classname;
    Classe classe;
    Button firstSemesterButton;
    Button secondSemesterButton;


    public ClassiViewHolder(@NonNull @NotNull View itemView, ClassSelected listener) {
        super(itemView);
        classname = (TextView) itemView.findViewById(R.id.ClassName);
        classname.setPaintFlags(classname.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        firstSemesterButton = (Button) itemView.findViewById(R.id.FirstSemesterButton);
        secondSemesterButton = (Button) itemView.findViewById(R.id.SecondSemesterButton);


        classname.setOnClickListener((View)-> listener.openChangeClassLocationDialog(classe));
        firstSemesterButton.setOnClickListener((v) -> listener.classSelect(classe,0));
        secondSemesterButton.setOnClickListener((v) -> listener.classSelect(classe,1));
    }

    public void setClasse(Classe classe){
        this.classe = classe;
        classname.setText(classe.getNomeClasse());
    }
}