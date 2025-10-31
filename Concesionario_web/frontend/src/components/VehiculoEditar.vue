<template>
  <div>
    <h2>Modificar Kms</h2>
    <input v-model="matricula" placeholder="Matrícula" />
    <input v-model.number="kms" type="number" placeholder="Nuevo Kms" />
    <button @click="modificarKms">Actualizar</button>
    <p v-if="mensaje">{{ mensaje }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      matricula: '',
      kms: 0,
      mensaje: ''
    }
  },
  methods: {
    async modificarKms() {
      try {
        const res = await fetch(`http://localhost:3000/vehiculos/${this.matricula}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ kms: this.kms })
        });
        const data = await res.json();
        if (!res.ok) this.mensaje = data.error;
        else this.mensaje = 'Kms actualizado correctamente';
      } catch (e) {
        this.mensaje = 'Error conectando con el servidor';
      }
    }
  }
}
</script>
