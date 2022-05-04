<template>

            <v-card flat>
              <v-card-text>
                <label for="id">Project id:</label><br />
                <v-divider></v-divider>
                <v-form class="px-3">
                  <v-text-field label="id" v-model="id"></v-text-field>
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
        }
    },
    methods: {
        checkOut() {
        console.log(this.id);
        fetch("http://localhost:3030/api/getIfc?fileName=" + this.id);
        },
    }
}
</script>