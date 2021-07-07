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
import com.example.unccrudapp.model.Store;
import com.example.unccrudapp.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;
    private JsonArrayRequest arrayRequest;
    private Context context;
    private ArrayList<Store> store;
    private String url = "";
    private StoreAdapter storeAdapter;
    private RecyclerView recyclerView;

    public StoreAdapter(Context context, ArrayList<Store> store) {
        this.context = context;
        this.store = store;
    }

    @NonNull
    public StoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.store_list, parent, false);

        return new StoreAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.MyViewHolder holder, int position) {
        holder.txtStore.setText(store.get(position).getNome());
        holder.txtNumber.setText(String.valueOf(position + 1));
        holder.edtStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = store.get(position).getId();
                String nome = store.get(position).getNome();
                String endereco = store.get(position).getEndereco();
                String estado = store.get(position).getEstado();
                String cidade = store.get(position).getCidade();
                String cnpj = store.get(position).getCnpj();

                JSONObject object = new JSONObject();
                try {
                    object.put("_id", id);
                    object.put("nome", nome);
                    object.put("endereco", endereco);
                    object.put("estado", estado);
                    object.put("cidade", cidade);
                    object.put("cnpj", cnpj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editStore(id, object);
            }
        });
        holder.deleteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = store.get(position).getId();
                deleteStore(id);
            }
        });
    }

    private void deleteStore(final String id) {
        TextView txtStore, txtClose;
        Button btnSave;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.store_delete);

        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        txtStore = (TextView) dialog.findViewById(R.id.txtStore);

        txtStore.setText("Excluir Loja");

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave = (Button) dialog.findViewById(R.id.btnDelete);
        String storeId = id;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(dialog, storeId);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void delete(Dialog dialog, String userId) {
        String url = "https://agile-wave-78775.herokuapp.com/lojas/delete/" + userId;
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

    private void editStore(final String id, JSONObject object) {
        TextView txtStore, txtClose;
        EditText edtName, edtEndereco, edtEstado, edtCidade, edtCnpj;
        Button btnSave;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.activity_modstore);

        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        txtStore = (TextView) dialog.findViewById(R.id.txtStore);

        txtStore.setText("Alterar Loja");

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        edtName = (EditText) dialog.findViewById(R.id.edtName);
        edtEndereco = (EditText) dialog.findViewById(R.id.edtEndereco);
        edtEstado = (EditText) dialog.findViewById(R.id.edtEstado);
        edtCidade = (EditText) dialog.findViewById(R.id.edtCidade);
        edtCnpj = (EditText) dialog.findViewById(R.id.edtCnpj);

        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        String storeId = null;
        try {
            storeId  = object.getString("_id");
            edtName.setText(object.getString("nome"));
            edtEndereco.setText(object.getString("endereco"));
            edtCidade.setText(object.getString("cidade"));
            edtEstado.setText(object.getString("estado"));
            edtCnpj.setText(object.getString("cnpj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String finalStoreId = storeId;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("_id", finalStoreId);
                    object.put("nome", edtName.getText());
                    object.put("endereco", edtEndereco.getText());
                    object.put("estado", edtEstado.getText());
                    object.put("cidade", edtCidade.getText());
                    object.put("cnpj", edtCnpj.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                submit(object, dialog, finalStoreId);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void submit(final JSONObject object, final Dialog dialog, String id) {
        String url = "https://agile-wave-78775.herokuapp.com/lojas/update/" + id;
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
                        Store s = new Store();
                        s.setId(jsonObject.getString("_id"));
                        s.setNome(jsonObject.getString("name"));
                        s.setEndereco(jsonObject.getString("endereco"));
                        s.setCidade(jsonObject.getString("cidade"));
                        s.setEstado(jsonObject.getString("estado"));
                        s.setCnpj(jsonObject.getString("cnpj"));
                        store.add(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterPush(store);
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

    private void adapterPush(ArrayList<Store> store) {
        //userAdapter = new UserAdapter(this, user);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(userAdapter);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtStore, txtNumber;
        private ImageView edtStore, deleteStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.idNumber);
            txtStore = (TextView) itemView.findViewById(R.id.nameStore);
            edtStore = (ImageView) itemView.findViewById(R.id.editStore);
            deleteStore = (ImageView) itemView.findViewById(R.id.deleteStore);
        }
    }

    @Override
    public int getItemCount() {
        return store.size();
    }
}
