<template>
  <div>
    <h2>Listado de Vehículos</h2>
    <button @click="cargarVehiculos">Refrescar</button>
    <table v-if="vehiculos.length">
      <thead>
        <tr>
          <th>Marca</th>
          <th>Matrícula</th>
          <th>Precio</th>
          <th>Kms</th>
          <th>Descripción</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="v in vehiculos" :key="v.matricula">
          <td>{{ v.marca }}</td>
          <td>{{ v.matricula }}</td>
          <td>{{ v.precio }}</td>
          <td>{{ v.kms }}</td>
          <td>{{ v.descripcion }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else>No hay vehículos registrados.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      vehiculos: []
    }
  },
  mounted() {
    this.cargarVehiculos();
  },
  methods: {
    async cargarVehiculos() {
      try {
        const res = await fetch('http://localhost:3000/vehiculos');
        this.vehiculos = await res.json();
      } catch (e) {
        alert('Error cargando vehículos');
      }
    }
  }
}
</script>
