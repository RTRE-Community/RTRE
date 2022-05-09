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
      color="green"
      @click="installWithOid()"
      >
      
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
    name: "CheckOutIconButton",
    props: ['oid'],
    methods: {
        installWithOid(){
            console.log(this.oid)
             fetch("http://localhost:3030/api/getIfc?fileName=" + this.oid);
        }
    },
}
</script>