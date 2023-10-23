package com.jonatan.church_mvc.Actividad;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jonatan.church_mvc.Ingreso.IngresoController;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.Utils.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActividadView extends AppCompatActivity {
    private TextView txtId;
    private EditText txtDescripcion, txtNombre,txtFInicio,txtFFinal,txtHInicio,txtHFinal;
    public Button btnCrear, btnGuardar, btnEliminar;
    public ListView actividadListView;
    private ArrayAdapter<ActividadDato> actividadArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        txtId = (TextView) findViewById(R.id.actividad_id);
        txtDescripcion = (EditText) findViewById(R.id.actividad_descripcion);
        txtNombre = (EditText) findViewById(R.id.actividad_nombre);
        txtFInicio = (EditText) findViewById(R.id.txtFechaInicio);
        txtFFinal = (EditText) findViewById(R.id.txtFechaFinal);
        txtHInicio = (EditText) findViewById(R.id.txtHoraInicio);
        txtHFinal = (EditText) findViewById(R.id.txtHoraFinal);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        actividadListView = (ListView) findViewById(R.id.actividad_list_view);
        actividadArrayAdapter = new ArrayAdapter<ActividadDato>(this, R.layout.list_item);
        actividadListView.setAdapter(actividadArrayAdapter);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ActividadModel actividadModel = new ActividadModel(db);
        ActividadController actividadController = new ActividadController(this, actividadModel);
    }

    public ActividadDato readFormData() throws ParseException {
        ActividadDato dato = new ActividadDato(
                txtId.getText().toString(),
                txtNombre.getText().toString(),
                txtDescripcion.getText().toString(),
                getFecha(txtFInicio.getText().toString(),txtHInicio.getText().toString()),
                getFecha(txtFFinal.getText().toString(),txtHFinal.getText().toString())
        );
        return dato;
    }
    private String getFecha(String fechaStartStr,String horaStartStr) throws ParseException {
//        String fechaStartStr = txtFInicio.getText().toString();
//        String horaStartStr = txtHInicio.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date fechaStart = dateFormat.parse(fechaStartStr);
        Date horaStart = timeFormat.parse(horaStartStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaStart);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date datetimeStart = new Date(calendar.getTimeInMillis() + horaStart.getTime());
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetimeStartStr = datetimeFormat.format(datetimeStart);
        return datetimeStartStr;
    }
    public void setFormData(ActividadDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtNombre.setText(dto.nombre);
        txtDescripcion.setText(dto.descripcion);
        txtFInicio.setText(dto.fecha_inicio);
        txtFFinal.setText(dto.fecha_final);
    }
    public void setRowsDataList(List<ActividadDato> list) {
        actividadArrayAdapter.clear();
        Log.d("rows", String.valueOf(list.size()));
        for (int i = 0; i < list.size(); i++) {
            actividadArrayAdapter.add(list.get(i));
        }
    }
    public void cleanFormData() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtFInicio.setText("");
        txtFFinal.setText("");
        txtHFinal.setText("");
        txtHInicio.setText("");
    }
}