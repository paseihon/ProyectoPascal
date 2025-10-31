<template>
  <div>
    <h2>Buscar Vehículo</h2>
    <input v-model="matricula" placeholder="Ingrese matrícula" />
    <button @click="buscarVehiculo">Buscar</button>

    <div v-if="vehiculo">
      <p>Marca: {{ vehiculo.marca }}</p>
      <p>Matrícula: {{ vehiculo.matricula }}</p>
      <p>Precio: {{ vehiculo.precio }}</p>
      <p>Kms: {{ vehiculo.kms }}</p>
      <p>Descripción: {{ vehiculo.descripcion }}</p>
    </div>
    <p v-else-if="mensaje">{{ mensaje }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      matricula: '',
      vehiculo: null,
      mensaje: ''
    }
  },
  methods: {
    async buscarVehiculo() {
      try {
        const res = await fetch(`http://localhost:3000/vehiculos/${this.matricula}`);
        if (!res.ok) {
          const data = await res.json();
          this.vehiculo = null;
          this.mensaje = data.error;
        } else {
          this.vehiculo = await res.json();
          this.mensaje = '';
        }
      } catch (e) {
        this.mensaje = 'Error conectando con el servidor';
      }
    }
  }
}
</script>
