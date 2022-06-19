package com.example.l11_chengbincai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView orderInformation;
    OrderDishDao orderDishDao;
    ArrayAdapter<CustomerOrder> adapter;
    List<String> ordernames = new ArrayList<>();

    public void showOrderWithDishes(String orderName){
        OrderWithDishes orderWithDishes = orderDishDao.getOrderWithDishes(orderName);
        String text = orderWithDishes.getCustomerOrder().getOrderName()+": ";
        text = text + orderWithDishes.getCustomerOrder().getOrderStatus() + "\n";
        int totalPrice = 0;
        for(int i = 0;i < orderWithDishes.getDishes().size();i++){
            text = text + (i + 1);
            text = text + " " + orderWithDishes.getDishes().get(i).getDishName();
            text = text + " " + orderWithDishes.getDishes().get(i).getDishPrice() + "\n";
            totalPrice = totalPrice + orderWithDishes.getDishes().get(i).getDishPrice();
        }
        text = text + "Total Price：" + totalPrice;
        orderInformation.setText(text);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String content = adapterView.getItemAtPosition(i).toString();
        if(adapterView.getId() == R.id.spinner){
            showOrderWithDishes(content);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        orderInformation.setText("Order Information");
    }

    private void initDishes(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("DishesExit", false)){
            Dish[] dishes = new Dish[10];
            dishes[0] = new Dish("红烧猪蹄", 30);
            dishes[1] = new Dish("澳洲龙虾", 20);
            dishes[2] = new Dish("沙茶面", 40);
            dishes[3] = new Dish("炸鸡", 50);
            dishes[4] = new Dish("薯条", 60);
            dishes[5] = new Dish("汉堡", 70);
            dishes[6] = new Dish("柠檬水", 80);
            dishes[7] = new Dish("可乐", 3);
            dishes[8] = new Dish("羊肉串", 100);
            dishes[9] = new Dish("鲍鱼", 300);
            orderDishDao.insertMultiDishes(dishes);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("DishesExit", true);
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newOrder = findViewById(R.id.newOrder);
        Button finishOrder = findViewById(R.id.finishOrder);
        Button deleteOrder = findViewById(R.id.deleteOrder);
        Spinner spinner = findViewById(R.id.spinner);
        orderInformation = findViewById(R.id.orderInformation);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,
                "restaurant_db").allowMainThreadQueries().build();
        orderDishDao = db.orderDishDao();

        initDishes();

        List<CustomerOrder> orderList = orderDishDao.loadAllCustomerOrder();
        for(int i = 0;i < orderList.size();i++){
            ordernames.add(orderList.get(i).getOrderName());
        }

        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item,ordernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        newOrder.setOnClickListener(v ->{
            CustomerOrder customerOrder = new CustomerOrder("order_" +
                    (int) (Math.random()*10000),"Preparing");
            long orderId = orderDishDao.insertOneOrder(customerOrder);
            OrderDishCrossRef[] orderDishCrossRefs = new OrderDishCrossRef[3];

            orderDishCrossRefs[0] = new OrderDishCrossRef(orderId, (int) (Math.random() * 3));
            orderDishCrossRefs[1] = new OrderDishCrossRef(orderId, (int) (Math.random() * 3 + 3));
            orderDishCrossRefs[2] = new OrderDishCrossRef(orderId, (int) (Math.random() * 3 + 6));

            orderDishDao.insertMultiDishesForOneOrder(orderDishCrossRefs);
            ordernames.add(customerOrder.getOrderName());
            adapter.notifyDataSetChanged();
            spinner.setSelection(ordernames.size());
        });

        finishOrder.setOnClickListener(v -> {
            if (ordernames.size()>0){
                String currentOrderName = spinner.getSelectedItem().toString();
                CustomerOrder currentOrder = orderDishDao.loadOneCustomerOrder(currentOrderName);
                currentOrder.setOrderStatus("Finished");
                orderDishDao.updateOneOrder(currentOrder);
                showOrderWithDishes(currentOrder.getOrderName());
            }
        });

        deleteOrder.setOnClickListener(v ->{
            if(ordernames.size()>0){
                String currentOrderName = spinner.getSelectedItem().toString();
                CustomerOrder currentOrder = orderDishDao.loadOneCustomerOrder(currentOrderName);

                orderDishDao.deleteOneOrder(currentOrder);

                ordernames.remove(currentOrderName);

                adapter.notifyDataSetChanged();
                spinner.setSelection(ordernames.size());
            }
        });
    }
}