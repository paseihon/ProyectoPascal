<template>
  <div>
    <h2>Nuevo Vehículo</h2>
    <form @submit.prevent="guardarVehiculo">
      <input v-model="marca" placeholder="Marca" required />
      <input v-model="matricula" placeholder="Matrícula (1234ABC)" required />
      <input v-model.number="precio" type="number" placeholder="Precio" required />
      <input v-model.number="kms" type="number" placeholder="Kilómetros" required />
      <textarea v-model="descripcion" placeholder="Descripción"></textarea>
      <button type="submit">Guardar</button>
    </form>
    <p v-if="mensaje">{{ mensaje }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      marca: '',
      matricula: '',
      precio: 0,
      kms: 0,
      descripcion: '',
      mensaje: ''
    }
  },
  methods: {
    async guardarVehiculo() {
      try {
        const res = await fetch('http://localhost:3000/vehiculos', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            marca: this.marca,
            matricula: this.matricula,
            precio: this.precio,
            kms: this.kms,
            descripcion: this.descripcion
          })
        });
        const data = await res.json();
        if (!res.ok) this.mensaje = data.error;
        else this.mensaje = 'Vehículo guardado correctamente';
      } catch (e) {
        this.mensaje = 'Error conectando con el servidor';
      }
    }
  }
}
</script>
