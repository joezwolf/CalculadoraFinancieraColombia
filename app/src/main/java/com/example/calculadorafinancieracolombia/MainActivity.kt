package com.example.calculadorafinancieracolombia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculadorafinancieracolombia.ui.theme.CalculadoraFinancieraColombiaTheme
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraFinancieraColombiaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainScreen(modifier = Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Lista de categorías para el DropdownMenu
    val categories = listOf("Productos", "Empleador", "Empleado")
    var selectedCategory by remember { mutableStateOf("Productos") }
    var result by remember { mutableStateOf("") }

    // 1. Estado para historial de cálculos
    val calculationHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Calculadora Financiera - Colombia",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Menú desplegable para seleccionar categoría
        CategorySelector(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Llamamos a las funciones de cálculo y agregamos el resultado a la lista
        when (selectedCategory) {
            "Productos" -> ProductCalculations { newResult ->
                result = newResult
                calculationHistory.add(newResult)
            }
            "Empleador" -> EmployerCalculations { newResult ->
                result = newResult
                calculationHistory.add(newResult)
            }
            "Empleado" -> EmployeeCalculations { newResult ->
                result = newResult
                calculationHistory.add(newResult)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el último resultado
        Text(
            text = "Resultado: $result",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Mostrar el historial de cálculos
        CalculationHistory(history = calculationHistory)
    }
}


@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String, // Recibe la categoría seleccionada
    onCategorySelected: (String) -> Unit // Callback para notificar el cambio
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedCategory.ifEmpty { "Seleccionar Categoría" })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category) // Notifica el cambio al componente padre
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProductCalculations(onResult: (String) -> Unit) {
    var priceBase by remember { mutableStateOf("") }
    var priceSale by remember { mutableStateOf("") }
    var costProduct by remember { mutableStateOf("") }
    var fixedCosts by remember { mutableStateOf("") }
    var variableCosts by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }

    var result by remember { mutableStateOf("") } // Estado del resultado

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Cálculos de Productos",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo para Precio Base
        OutlinedTextField(
            value = priceBase,
            onValueChange = { priceBase = it },
            label = { Text("Precio Base (sin IVA)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Precio de Venta
        OutlinedTextField(
            value = priceSale,
            onValueChange = { priceSale = it },
            label = { Text("Precio de Venta (unitario)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Costo del Producto
        OutlinedTextField(
            value = costProduct,
            onValueChange = { costProduct = it },
            label = { Text("Costo del Producto (unitario)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Costos Fijos
        OutlinedTextField(
            value = fixedCosts,
            onValueChange = { fixedCosts = it },
            label = { Text("Costos Fijos") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Costo Variable Unitario
        OutlinedTextField(
            value = variableCosts,
            onValueChange = { variableCosts = it },
            label = { Text("Costo Variable Unitario") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Inversión Total
        OutlinedTextField(
            value = investment,
            onValueChange = { investment = it },
            label = { Text("Inversión Total") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para Ingresos Totales
        OutlinedTextField(
            value = income,
            onValueChange = { income = it },
            label = { Text("Ingresos Totales") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Calcular Precio con IVA
        Button(
            onClick = {
                val base = priceBase.toDoubleOrNull() ?: 0.0
                val precioIVA = base * 1.19
                result = "Precio con IVA: ${precioIVA.format(2)}"
                onResult(result)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Calcular Precio con IVA")
        }

        // Botón para Calcular Margen de Ganancia
        Button(
            onClick = {
                val venta = priceSale.toDoubleOrNull() ?: 0.0
                val costo = costProduct.toDoubleOrNull() ?: 0.0
                result = if (venta > 0) {
                    val margen = ((venta - costo) / venta) * 100
                    "Margen de Ganancia: ${margen.format(2)}%"
                } else {
                    "Error: Precio de Venta debe ser mayor a 0"
                }
                onResult(result)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Calcular Margen de Ganancia")
        }

        // Botón para Calcular Punto de Equilibrio
        Button(
            onClick = {
                val fijos = fixedCosts.toDoubleOrNull() ?: 0.0
                val venta = priceSale.toDoubleOrNull() ?: 0.0
                val variable = variableCosts.toDoubleOrNull() ?: 0.0
                result = if (venta > variable) {
                    val equilibrio = fijos / (venta - variable)
                    "Punto de Equilibrio: ${equilibrio.format(2)} unidades"
                } else {
                    "Error: Precio de Venta debe ser mayor al Costo Variable Unitario"
                }
                onResult(result)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Calcular Punto de Equilibrio")
        }

        // Botón para Calcular ROI
        Button(
            onClick = {
                val inv = investment.toDoubleOrNull() ?: 0.0
                val ing = income.toDoubleOrNull() ?: 0.0
                result = if (inv > 0) {
                    val roi = ((ing - inv) / inv) * 100
                    "ROI: ${roi.format(2)}%"
                } else {
                    "Error: La Inversión debe ser mayor a 0"
                }
                onResult(result)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Calcular ROI")
        }

        // Mostrar el resultado final
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = result,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

// Extensión para formatear números con dos decimales
fun Double.format(digits: Int) = "%.${digits}f".format(this)

@Composable
fun EmployerCalculations(onResult: (String) -> Unit) {
    var salaryBase by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Cálculos de Empleador",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = salaryBase,
            onValueChange = { salaryBase = it },
            label = { Text("Salario Base") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val salary = salaryBase.toDoubleOrNull()
            if (salary == null) {
                onResult("Error: El salario ingresado no es un número válido.")
                return@Button
            }


            // Cálculos
            val aportesParafiscales = salary * 0.09       // 9%
            val seguridadSocial = salary * 0.205          // 20.5%
            val prestaciones = salary * 0.2183            // 21.83%
            val costoTotalNomina = salary + aportesParafiscales + seguridadSocial + prestaciones

            val resultado = """
                Aportes parafiscales: ${aportesParafiscales.format(2)}
                Seguridad social: ${seguridadSocial.format(2)}
                Prestaciones sociales: ${prestaciones.format(2)}
                
                Costo total de nómina: ${costoTotalNomina.format(2)}
            """.trimIndent()

            onResult(resultado)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Calcular")
        }
    }
}

@Composable
fun EmployeeCalculations(onResult: (String) -> Unit) {
    var salaryBase by remember { mutableStateOf("") }
    var extraHoursDay by remember { mutableStateOf("") }
    var extraHoursNight by remember { mutableStateOf("") }
    var extraHoursHoliday by remember { mutableStateOf("") }
    var bonuses by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Cálculos de Empleado",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = salaryBase,
            onValueChange = { salaryBase = it },
            label = { Text("Salario Base") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = extraHoursDay,
            onValueChange = { extraHoursDay = it },
            label = { Text("Horas Extras Diurnas") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = extraHoursNight,
            onValueChange = { extraHoursNight = it },
            label = { Text("Horas Extras Nocturnas") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = extraHoursHoliday,
            onValueChange = { extraHoursHoliday = it },
            label = { Text("Horas Dominicales/Festivas") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bonuses,
            onValueChange = { bonuses = it },
            label = { Text("Bonificaciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val salary = salaryBase.toDoubleOrNull()
                if (salary == null) {
                    onResult("Error: El salario ingresado no es un número válido.")
                    return@Button
                }

                val dayHours = extraHoursDay.toDoubleOrNull()
                if (dayHours == null) {
                    onResult("Error: Las horas extras diurnas ingresadas no son válidas.")
                    return@Button
                }

                val nightHours = extraHoursNight.toDoubleOrNull()
                if (nightHours == null) {
                    onResult("Error: Las horas extras nocturnas ingresadas no son válidas.")
                    return@Button
                }

                val holidayHours = extraHoursHoliday.toDoubleOrNull()
                if (holidayHours == null) {
                    onResult("Error: Las horas festivas/dominicales ingresadas no son válidas.")
                    return@Button
                }

                val bonus = bonuses.toDoubleOrNull()
                if (bonus == null) {
                    onResult("Error: La bonificación ingresada no es válida.")
                    return@Button
                }

                // Cálculo de las deducciones (pensión 4% + salud 4%)
                val deductions = salary * 0.08

                // Cálculo valor de cada tipo de hora extra
                val extraDayRate = (salary / 240.0) * 1.25
                val extraNightRate = (salary / 240.0) * 1.75
                val extraHolidayRate = (salary / 240.0) * 2.0

                // Cálculo total de horas extras
                val totalExtra = (dayHours * extraDayRate) + (nightHours * extraNightRate) + (holidayHours * extraHolidayRate)

                // Salario neto final
                val netSalary = (salary + totalExtra + bonus) - deductions

                val resultado = """
                    Deducciones de nómina (Pensión + Salud): ${deductions.format(2)}
                    Total Horas Extras: ${totalExtra.format(2)}
                    Bonificaciones: ${bonus.format(2)}
                    
                    Salario Neto: ${netSalary.format(2)}
                """.trimIndent()

                onResult(resultado)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}

@Composable
fun CalculationHistory(history: List<String>) {
    // Si hay muchos resultados, se aconseja un LazyColumn
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Historial de Cálculos",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        history.forEachIndexed { index, item ->
            Text(
                text = "${index + 1}. $item",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}



