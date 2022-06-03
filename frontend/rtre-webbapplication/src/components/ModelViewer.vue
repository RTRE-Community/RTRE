<template>
<v-app class="pb10">
    <input type="file" name="load" id="file-input" />
    <v-card id="wrapper" style="width: 55vw;height: 55vh;" elevation="3" class="mt-5">
        <canvas id="three-canvas"></canvas>
    </v-card>
</v-app>
</template>

<script>
import {
    AmbientLight,
    AxesHelper,
    DirectionalLight,
    GridHelper,
    PerspectiveCamera,
    Scene,
    WebGLRenderer,
} from "three";
import {
    OrbitControls
} from "three/examples/jsm/controls/OrbitControls";
import {
    IFCLoader
} from "web-ifc-three/IFCLoader";
export default {
    name: "ModelViewer",
    data() {
        return {
            LatestIfcModel: null
        }
    },
    mounted() {

        let vCardWrapper = document.getElementById('wrapper');
        let vCardWrapperDimensions = vCardWrapper.getBoundingClientRect();
        //Creates the Three.js scene
        const scene = new Scene();
        //Object to store the size of the viewport
        const size = {
            width: vCardWrapperDimensions.width,
            height: vCardWrapperDimensions.height
        };

        //Creates the camera (point of view of the user)
        const aspect = size.width / size.height;
        const camera = new PerspectiveCamera(75, aspect, 0.1, 1000);
        camera.position.z = 15;
        camera.position.y = 13;
        camera.position.x = 8;

        //Creates the lights of the scene
        const lightColor = 0xffffff;

        const ambientLight = new AmbientLight(lightColor, 0.5);
        scene.add(ambientLight);

        const directionalLight = new DirectionalLight(lightColor, 1);
        directionalLight.position.set(0, 10, 0);
        directionalLight.target.position.set(-5, 0, 0);
        scene.add(directionalLight);
        scene.add(directionalLight.target);

        //Sets up the renderer, fetching the canvas of the HTML
        const threeCanvas = document.getElementById("three-canvas");
        const renderer = new WebGLRenderer({
            canvas: threeCanvas,
            alpha: true
        });

        renderer.setSize(size.width, size.height);
        renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

        //Creates grids and axes in the scene
        const grid = new GridHelper(50, 30);
        scene.add(grid);

        const axes = new AxesHelper();
        axes.material.depthTest = false;
        axes.renderOrder = 1;
        scene.add(axes);

        //Creates the orbit controls (to navigate the scene)
        const controls = new OrbitControls(camera, threeCanvas);
        controls.enableDamping = true;
        controls.target.set(-2, 0, 0);

        //Animation loop
        const animate = () => {
            controls.update();
            renderer.render(scene, camera);
            requestAnimationFrame(animate);
        };

        animate();

        //Adjust the viewport to the size of the browser
        window.addEventListener("resize", () => {
            let vCardWrapper = document.getElementById('wrapper');
            let vCardWrapperDimensions = vCardWrapper.getBoundingClientRect();
            size.width = window.innerWidth;
            size.height = window.innerHeight;
            camera.aspect = vCardWrapperDimensions.width / vCardWrapperDimensions.height;
            camera.updateProjectionMatrix();
            renderer.setSize( vCardWrapperDimensions.width, vCardWrapperDimensions.height);
        });
        const ifcLoader = new IFCLoader();
        ifcLoader.ifcManager.setWasmPath("../");
        const input = document.getElementById("file-input");
        input.addEventListener(
            "change",
            (changed) => {
                const file = changed.target.files[0];
                var ifcURL = URL.createObjectURL(file);
                this.renderModel(ifcURL, scene)
            },
            false
        );
    },
    methods: {
        renderModel(ifcURL, scene) {

            const ifcLoader = new IFCLoader();
            ifcLoader.ifcManager.setWasmPath("../");

            ifcLoader.load(
                ifcURL,
                (ifcModel) => scene.add(ifcModel));
            //Creates the Three.js scene
        }
    }
}
</script>
