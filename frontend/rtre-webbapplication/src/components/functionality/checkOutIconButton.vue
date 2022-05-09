<template>
 <v-tooltip bottom>
      <template v-slot:activator="{on , attrs}">
    <v-btn
    class="mx-2"
      fab
      dark
      small
      v-bind="attrs"
      v-on="on"
      color="green">
      
        <v-icon dark> 
            mdi-cloud-download
        </v-icon>
    </v-btn>
    </template>
      <span>Install</span>
    </v-tooltip>
</template>

<script>
export default {
    name: "CheckOutIconButton"
}
</script>