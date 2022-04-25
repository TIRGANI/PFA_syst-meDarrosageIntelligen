/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FermUpdateComponent from '@/entities/ferm/ferm-update.vue';
import FermClass from '@/entities/ferm/ferm-update.component';
import FermService from '@/entities/ferm/ferm.service';

import ParcelleService from '@/entities/parcelle/parcelle.service';
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
  describe('Ferm Management Update Component', () => {
    let wrapper: Wrapper<FermClass>;
    let comp: FermClass;
    let fermServiceStub: SinonStubbedInstance<FermService>;

    beforeEach(() => {
      fermServiceStub = sinon.createStubInstance<FermService>(FermService);

      wrapper = shallowMount<FermClass>(FermUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          fermService: () => fermServiceStub,
          alertService: () => new AlertService(),

          parcelleService: () =>
            sinon.createStubInstance<ParcelleService>(ParcelleService, {
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
        comp.ferm = entity;
        fermServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(fermServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.ferm = entity;
        fermServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(fermServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFerm = { id: 123 };
        fermServiceStub.find.resolves(foundFerm);
        fermServiceStub.retrieve.resolves([foundFerm]);

        // WHEN
        comp.beforeRouteEnter({ params: { fermId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ferm).toBe(foundFerm);
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
