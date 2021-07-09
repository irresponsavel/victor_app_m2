package com.example.unccrudapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unccrudapp.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;
    private JsonArrayRequest arrayRequest;
    private Context context;
    private ArrayList<Item> item;
    private String url = "https://agile-wave-78775.herokuapp.com/produtos";
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;

    public ItemAdapter(Context context, ArrayList<Item> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_list, parent, false);

        return new ItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        holder.txtItem.setText(item.get(position).getNome());
        holder.txtNumber.setText(String.valueOf(position + 1));
        holder.edtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.get(position).getId();
                String nome = item.get(position).getNome();
                String preco = item.get(position).getPreco();
                String categoria = item.get(position).getCategoria();
                String descricao = item.get(position).getDescricao();

                JSONObject object = new JSONObject();
                try {
                    object.put("_id", id);
                    object.put("nome", nome);
                    object.put("preco", preco);
                    object.put("categoria", categoria);
                    object.put("descricao", descricao);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editItem(id, object);
            }
        });
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.get(position).getId();
                deleteItem(id);
            }
        });
    }

    private void deleteItem(final String id) {
        TextView txtItem, txtClose;
        Button btnSave;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.item_delete);

        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        txtItem = (TextView) dialog.findViewById(R.id.txtItem);

        txtItem.setText("Excluir Item");

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave = (Button) dialog.findViewById(R.id.btnDelete);
        String ItemId = id;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(dialog, ItemId);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void delete(Dialog dialog, String userId) {
        String url = "https://agile-wave-78775.herokuapp.com/produtos/delete/" + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(context, "Dados excluídos com sucesso!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) { };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void editItem(final String id, JSONObject object) {
        TextView txtItem, txtClose;
        EditText edtName, edtPreco, edtCategoria, edtDescricao;
        Button btnSave;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.activity_moditem);

        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        txtItem = (TextView) dialog.findViewById(R.id.txtItem);

        txtItem.setText("Alterar Produto");

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        edtName = (EditText) dialog.findViewById(R.id.edtName);
        edtPreco = (EditText) dialog.findViewById(R.id.edtPreco);
        edtCategoria = (EditText) dialog.findViewById(R.id.edtCategoria);
        edtDescricao = (EditText) dialog.findViewById(R.id.edtDescricao);

        //desabilitando o campo nome
        edtName.setEnabled(false);

        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        String itemId = null;
        try {
            itemId  = object.getString("_id");
            edtName.setText(object.getString("nome"));
            edtPreco.setText(object.getString("preco"));
            edtCategoria.setText(object.getString("categoria"));
            edtDescricao.setText(object.getString("descricao"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String finalItemId = itemId;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("_id", finalItemId);
                    object.put("nome", edtName.getText());
                    object.put("preco", edtPreco.getText());
                    object.put("categoria", edtCategoria.getText());
                    object.put("descricao", edtDescricao.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                submit(object, dialog, finalItemId);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void submit(final JSONObject object, final Dialog dialog, String id) {
        String url = "https://agile-wave-78775.herokuapp.com/produtos/update/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                /*
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        user.clear();
                        getData();
                    }
                });
                 */
                Toast.makeText(context, "Dados alterados com sucesso!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Erro ao alterar dados!", Toast.LENGTH_LONG).show();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) { };

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void getData() {
        refreshLayout.setRefreshing(true);

        arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Item it = new Item();
                        it.setId(jsonObject.getString("_id"));
                        it.setNome(jsonObject.getString("name"));
                        it.setPreco(jsonObject.getString("preco"));
                        it.setCategoria(jsonObject.getString("categoria"));
                        it.setDescricao(jsonObject.getString("descricao"));
                        item.add(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterPush(item);
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //requestQueue = Volley.newRequestQueue(MainActivity.this);
        //requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Item> item) {
        //userAdapter = new UserAdapter(this, user);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(userAdapter);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItem, txtNumber;
        private ImageView edtItem, deleteItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.idNumber);
            txtItem = (TextView) itemView.findViewById(R.id.nameItem);
            edtItem = (ImageView) itemView.findViewById(R.id.editItem);
            deleteItem = (ImageView) itemView.findViewById(R.id.deleteItem);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}
