<template>

            <v-card flat>
              <v-card-text>
                <v-form>
                  <v-text-field label="id" v-model="id"></v-text-field>
                  <v-select
                    :items="items"
                    v-model="selectedFormat"
                    label="Ifc format - WIP"
                    dense
                    outlined
                   ></v-select>
                  <v-btn text class="blue mx-0 mt-3" @click="checkOut"
                    >Get Project</v-btn
                  >
                </v-form>
              </v-card-text>
            </v-card>
    
</template>

<script>
export default {
    name:"checkOut",
    data(){
        return{
          id: "",
          selectedFormat: "",
          items:["Ifc4", "Ifc2x3tc1"]
        }
    },
    methods: {
        checkOut() {
        console.log(this.id);
        fetch("http://localhost:3030/api/getIfc?fileName=" + this.id
        +"&schema="+ this.selectedFormat);
        },
    }
}
</script>