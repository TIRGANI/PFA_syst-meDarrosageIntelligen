/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TypePlantUpdateComponent from '@/entities/type-plant/type-plant-update.vue';
import TypePlantClass from '@/entities/type-plant/type-plant-update.component';
import TypePlantService from '@/entities/type-plant/type-plant.service';

import PlanteService from '@/entities/plante/plante.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('TypePlant Management Update Component', () => {
    let wrapper: Wrapper<TypePlantClass>;
    let comp: TypePlantClass;
    let typePlantServiceStub: SinonStubbedInstance<TypePlantService>;

    beforeEach(() => {
      typePlantServiceStub = sinon.createStubInstance<TypePlantService>(TypePlantService);

      wrapper = shallowMount<TypePlantClass>(TypePlantUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          typePlantService: () => typePlantServiceStub,
          alertService: () => new AlertService(),

          planteService: () =>
            sinon.createStubInstance<PlanteService>(PlanteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.typePlant = entity;
        typePlantServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(typePlantServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.typePlant = entity;
        typePlantServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(typePlantServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTypePlant = { id: 123 };
        typePlantServiceStub.find.resolves(foundTypePlant);
        typePlantServiceStub.retrieve.resolves([foundTypePlant]);

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
