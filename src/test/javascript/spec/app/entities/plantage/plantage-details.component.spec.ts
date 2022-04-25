/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PlantageDetailComponent from '@/entities/plantage/plantage-details.vue';
import PlantageClass from '@/entities/plantage/plantage-details.component';
import PlantageService from '@/entities/plantage/plantage.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Plantage Management Detail Component', () => {
    let wrapper: Wrapper<PlantageClass>;
    let comp: PlantageClass;
    let plantageServiceStub: SinonStubbedInstance<PlantageService>;

    beforeEach(() => {
      plantageServiceStub = sinon.createStubInstance<PlantageService>(PlantageService);

      wrapper = shallowMount<PlantageClass>(PlantageDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { plantageService: () => plantageServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPlantage = { id: 123 };
        plantageServiceStub.find.resolves(foundPlantage);

        // WHEN
        comp.retrievePlantage(123);
        await comp.$nextTick();

        // THEN
        expect(comp.plantage).toBe(foundPlantage);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPlantage = { id: 123 };
        plantageServiceStub.find.resolves(foundPlantage);

        // WHEN
        comp.beforeRouteEnter({ params: { plantageId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.plantage).toBe(foundPlantage);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
