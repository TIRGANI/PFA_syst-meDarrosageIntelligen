/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PlanteUpdateComponent from '@/entities/plante/plante-update.vue';
import PlanteClass from '@/entities/plante/plante-update.component';
import PlanteService from '@/entities/plante/plante.service';

import TypePlantService from '@/entities/type-plant/type-plant.service';

import PlantageService from '@/entities/plantage/plantage.service';
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
  describe('Plante Management Update Component', () => {
    let wrapper: Wrapper<PlanteClass>;
    let comp: PlanteClass;
    let planteServiceStub: SinonStubbedInstance<PlanteService>;

    beforeEach(() => {
      planteServiceStub = sinon.createStubInstance<PlanteService>(PlanteService);

      wrapper = shallowMount<PlanteClass>(PlanteUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          planteService: () => planteServiceStub,
          alertService: () => new AlertService(),

          typePlantService: () =>
            sinon.createStubInstance<TypePlantService>(TypePlantService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          plantageService: () =>
            sinon.createStubInstance<PlantageService>(PlantageService, {
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
        comp.plante = entity;
        planteServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planteServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.plante = entity;
        planteServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPlante = { id: 123 };
        planteServiceStub.find.resolves(foundPlante);
        planteServiceStub.retrieve.resolves([foundPlante]);

        // WHEN
        comp.beforeRouteEnter({ params: { planteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.plante).toBe(foundPlante);
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
