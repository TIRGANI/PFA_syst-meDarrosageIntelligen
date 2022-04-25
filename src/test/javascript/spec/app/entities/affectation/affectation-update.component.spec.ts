/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AffectationUpdateComponent from '@/entities/affectation/affectation-update.vue';
import AffectationClass from '@/entities/affectation/affectation-update.component';
import AffectationService from '@/entities/affectation/affectation.service';

import BoitierService from '@/entities/boitier/boitier.service';

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
  describe('Affectation Management Update Component', () => {
    let wrapper: Wrapper<AffectationClass>;
    let comp: AffectationClass;
    let affectationServiceStub: SinonStubbedInstance<AffectationService>;

    beforeEach(() => {
      affectationServiceStub = sinon.createStubInstance<AffectationService>(AffectationService);

      wrapper = shallowMount<AffectationClass>(AffectationUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          affectationService: () => affectationServiceStub,
          alertService: () => new AlertService(),

          boitierService: () =>
            sinon.createStubInstance<BoitierService>(BoitierService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

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
        comp.affectation = entity;
        affectationServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(affectationServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.affectation = entity;
        affectationServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(affectationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAffectation = { id: 123 };
        affectationServiceStub.find.resolves(foundAffectation);
        affectationServiceStub.retrieve.resolves([foundAffectation]);

        // WHEN
        comp.beforeRouteEnter({ params: { affectationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.affectation).toBe(foundAffectation);
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
