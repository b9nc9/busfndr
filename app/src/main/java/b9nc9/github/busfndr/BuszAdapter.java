package b9nc9.github.busfndr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.List;

public class BuszAdapter extends RecyclerView.Adapter<BuszAdapter.BuszViewHolder> {

    Context bContext;
    List<Busz> bData;

    public BuszAdapter(Context bContext, List<Busz> bData){
        this.bContext = bContext;
        this.bData = bData;
    }

    @NonNull
    @Override
    public BuszViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(bContext).inflate(R.layout.item_busz,viewGroup, false);
        return new BuszViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull BuszViewHolder buszViewHolder, int position) {
        buszViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(bContext, R.anim.fade_in));

        buszViewHolder.idoi.setText(bData.get(position).indulIdo);
        buszViewHolder.idoe.setText(bData.get(position).erkezIdo);
        buszViewHolder.varos.setText(bData.get(position).indul);
        buszViewHolder.allomas.setText(bData.get(position).cel);
        buszViewHolder.megy.setText(bData.get(position).kozlekedik);
        buszViewHolder.varos.setSelected(true);
        buszViewHolder.megy.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return bData.size();
    }


    public class BuszViewHolder extends RecyclerView.ViewHolder{
        TextView idoi, idoe, varos, allomas, megy;

        public BuszViewHolder(@NonNull View ItemView){
            super(ItemView);
            idoi = itemView.findViewById(R.id.idoi);
            idoe = itemView.findViewById(R.id.idoe);
            varos = itemView.findViewById(R.id.varos);
            allomas = itemView.findViewById(R.id.megallo);
            megy = itemView.findViewById(R.id.jar);
        }
    }

}
