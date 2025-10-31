<template>
  <div>
    <h1>Concesionario de Coches</h1>
    <nav>
      <router-link to="/lista">Listar Vehículos</router-link> |
      <router-link to="/nuevo">Nuevo Vehículo</router-link> |
      <router-link to="/buscar">Buscar Vehículo</router-link> |
      <router-link to="/modificar">Modificar Kms</router-link>
    </nav>
    <router-view />
  </div>
</template>

<script>
export default {}
</script>

<style>
nav a {
  margin-right: 10px;
  text-decoration: none;
  color: blue;
}
nav a.router-link-exact-active {
  font-weight: bold;
}
</style>
