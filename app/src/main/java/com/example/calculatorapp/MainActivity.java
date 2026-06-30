package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private boolean resultDisplayed = false;
    private TextView txtExpression, txtResult;
    private TextView btn0, btn1, btn2, btn3, btn4,
            btn5, btn6, btn7, btn8, btn9;
    private TextView btnPlus, btnMinus, btnMultiply, btnDivide;
    private TextView btnAC, btnDEL, btnPercent, btnDot, btnEqual;

    private ListView listHistory;
    private TextView btnClearHistory;

    private ArrayList<String> historyList;
    private ArrayAdapter<String> historyAdapter;
    private String expression = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtExpression = findViewById(R.id.txtExpression);
        txtResult = findViewById(R.id.txtResult);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);

        btnAC = findViewById(R.id.btnAC);
        btnDEL = findViewById(R.id.btnDEL);
        btnPercent = findViewById(R.id.btnPercent);
        btnDot = findViewById(R.id.btnDot);
        btnEqual = findViewById(R.id.btnEqual);
        listHistory = findViewById(R.id.listHistory);
        btnClearHistory = findViewById(R.id.btnClearHistory);

        historyList = new ArrayList<>();

        historyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyList
        );

        listHistory.setAdapter(historyAdapter);
        btn0.setOnClickListener(v -> appendToExpression("0"));
        btn1.setOnClickListener(v -> appendToExpression("1"));
        btn2.setOnClickListener(v -> appendToExpression("2"));
        btn3.setOnClickListener(v -> appendToExpression("3"));
        btn4.setOnClickListener(v -> appendToExpression("4"));
        btn5.setOnClickListener(v -> appendToExpression("5"));
        btn6.setOnClickListener(v -> appendToExpression("6"));
        btn7.setOnClickListener(v -> appendToExpression("7"));
        btn8.setOnClickListener(v -> appendToExpression("8"));
        btn9.setOnClickListener(v -> appendToExpression("9"));

        btnPlus.setOnClickListener(v -> appendToExpression("+"));
        btnMinus.setOnClickListener(v -> appendToExpression("-"));
        btnMultiply.setOnClickListener(v -> appendToExpression("*"));
        btnDivide.setOnClickListener(v -> appendToExpression("/"));
        btnDot.setOnClickListener(v -> appendToExpression("."));

        btnAC.setOnClickListener(v -> {
            expression = "";
            txtExpression.setText("");
            txtResult.setText("0");
        });

        btnDEL.setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                txtExpression.setText(expression);
            }
        });

        btnEqual.setOnClickListener(v -> calculateResult());

        btnPercent.setOnClickListener(v -> {

            try {

                double value = Double.parseDouble(expression);

                value = value / 100;

                txtResult.setText(String.valueOf(value));

            } catch (Exception e) {

                txtResult.setText("Error");
            }
        });
        btnClearHistory.setOnClickListener(v -> {

            historyList.clear();

            historyAdapter.notifyDataSetChanged();
        });
        listHistory.setOnItemClickListener((parent, view, position, id) -> {

            String item = historyList.get(position);

            String result =
                    item.substring(item.indexOf("=") + 1).trim();

            expression = result;

            txtExpression.setText(result);

            txtResult.setText(result);
        });

    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private void appendToExpression(String value) {

        if (resultDisplayed) {

            if (!(value.equals("+")
                    || value.equals("-")
                    || value.equals("*")
                    || value.equals("/"))) {

                expression = "";
                txtExpression.setText("");
            }

            resultDisplayed = false;
        }

        if (!expression.isEmpty()) {

            char lastChar = expression.charAt(expression.length() - 1);

            if (isOperator(lastChar) &&
                    (value.equals("+")
                            || value.equals("-")
                            || value.equals("*")
                            || value.equals("/"))) {

                return;
            }
        }
        if (value.equals(".")) {

            String[] parts = expression.split("[+\\-*/]");

            if (parts.length > 0) {

                String currentNumber = parts[parts.length - 1];

                if (currentNumber.contains(".")) {
                    return;
                }
            }
        }


        expression += value;
        txtExpression.setText(expression);
    }
    private void calculateResult() {

        try {

            double result = 0;

            if (expression.contains("+")) {

                String[] parts = expression.split("\\+");

                result = Double.parseDouble(parts[0])
                        + Double.parseDouble(parts[1]);

            } else if (expression.contains("-")) {

                String[] parts = expression.split("-");

                result = Double.parseDouble(parts[0])
                        - Double.parseDouble(parts[1]);

            } else if (expression.contains("*")) {

                String[] parts = expression.split("\\*");

                result = Double.parseDouble(parts[0])
                        * Double.parseDouble(parts[1]);

            } else if (expression.contains("/")) {

                String[] parts = expression.split("/");

                result = Double.parseDouble(parts[0])
                        / Double.parseDouble(parts[1]);
            }

            if (result == (int) result) {
                txtResult.setText(String.valueOf((int) result));
            } else {
                txtResult.setText(String.valueOf(result));
            }
            resultDisplayed = true;

        } catch (Exception e) {

            txtResult.setText("Error");
        }
        String historyEntry =
                expression + " = " + txtResult.getText().toString();

        historyList.add(0, historyEntry);

        historyAdapter.notifyDataSetChanged();
    }
}