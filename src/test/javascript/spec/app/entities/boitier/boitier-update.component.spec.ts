/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import BoitierUpdateComponent from '@/entities/boitier/boitier-update.vue';
import BoitierClass from '@/entities/boitier/boitier-update.component';
import BoitierService from '@/entities/boitier/boitier.service';

import AlerteService from '@/entities/alerte/alerte.service';

import BracheService from '@/entities/brache/brache.service';

import AffectationService from '@/entities/affectation/affectation.service';
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
  describe('Boitier Management Update Component', () => {
    let wrapper: Wrapper<BoitierClass>;
    let comp: BoitierClass;
    let boitierServiceStub: SinonStubbedInstance<BoitierService>;

    beforeEach(() => {
      boitierServiceStub = sinon.createStubInstance<BoitierService>(BoitierService);

      wrapper = shallowMount<BoitierClass>(BoitierUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          boitierService: () => boitierServiceStub,
          alertService: () => new AlertService(),

          alerteService: () =>
            sinon.createStubInstance<AlerteService>(AlerteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          bracheService: () =>
            sinon.createStubInstance<BracheService>(BracheService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          affectationService: () =>
            sinon.createStubInstance<AffectationService>(AffectationService, {
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
        comp.boitier = entity;
        boitierServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(boitierServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.boitier = entity;
        boitierServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(boitierServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBoitier = { id: 123 };
        boitierServiceStub.find.resolves(foundBoitier);
        boitierServiceStub.retrieve.resolves([foundBoitier]);

        // WHEN
        comp.beforeRouteEnter({ params: { boitierId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.boitier).toBe(foundBoitier);
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
