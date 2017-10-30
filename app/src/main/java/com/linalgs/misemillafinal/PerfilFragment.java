package com.linalgs.misemillafinal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {
    private ImageView imageView;
    private TextView tContraseña, tNombre;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        String correo = getArguments().getString("correo");
        String contraseña = getArguments().getString("contraseña");
        String urlf = getArguments().getString("urlf");

        imageView = (ImageView)v.findViewById(R.id.imageView) ;
        tContraseña =(TextView) v.findViewById(R.id.tContraseña);
        tNombre =(TextView) v.findViewById(R.id.tNombre);

        tNombre.setText(correo);
        tContraseña.setText(contraseña);
        Glide.with(this).load(urlf).crossFade().placeholder(R.drawable.user).into(imageView);



        return v;
    }

}
