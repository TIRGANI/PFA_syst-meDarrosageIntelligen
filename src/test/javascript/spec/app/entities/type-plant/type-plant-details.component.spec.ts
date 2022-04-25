/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TypePlantDetailComponent from '@/entities/type-plant/type-plant-details.vue';
import TypePlantClass from '@/entities/type-plant/type-plant-details.component';
import TypePlantService from '@/entities/type-plant/type-plant.service';
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
  describe('TypePlant Management Detail Component', () => {
    let wrapper: Wrapper<TypePlantClass>;
    let comp: TypePlantClass;
    let typePlantServiceStub: SinonStubbedInstance<TypePlantService>;

    beforeEach(() => {
      typePlantServiceStub = sinon.createStubInstance<TypePlantService>(TypePlantService);

      wrapper = shallowMount<TypePlantClass>(TypePlantDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { typePlantService: () => typePlantServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTypePlant = { id: 123 };
        typePlantServiceStub.find.resolves(foundTypePlant);

        // WHEN
        comp.retrieveTypePlant(123);
        await comp.$nextTick();

        // THEN
        expect(comp.typePlant).toBe(foundTypePlant);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTypePlant = { id: 123 };
        typePlantServiceStub.find.resolves(foundTypePlant);

        // WHEN
        comp.beforeRouteEnter({ params: { typePlantId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.typePlant).toBe(foundTypePlant);
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
