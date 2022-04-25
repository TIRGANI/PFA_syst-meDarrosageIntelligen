/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ParcelleUpdateComponent from '@/entities/parcelle/parcelle-update.vue';
import ParcelleClass from '@/entities/parcelle/parcelle-update.component';
import ParcelleService from '@/entities/parcelle/parcelle.service';

import HistoriqueService from '@/entities/historique/historique.service';

import GrandeurService from '@/entities/grandeur/grandeur.service';

import AffectationService from '@/entities/affectation/affectation.service';

import FermService from '@/entities/ferm/ferm.service';

import PlantageService from '@/entities/plantage/plantage.service';

import TypeSolService from '@/entities/type-sol/type-sol.service';

import AlerteService from '@/entities/alerte/alerte.service';
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
  describe('Parcelle Management Update Component', () => {
    let wrapper: Wrapper<ParcelleClass>;
    let comp: ParcelleClass;
    let parcelleServiceStub: SinonStubbedInstance<ParcelleService>;

    beforeEach(() => {
      parcelleServiceStub = sinon.createStubInstance<ParcelleService>(ParcelleService);

      wrapper = shallowMount<ParcelleClass>(ParcelleUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          parcelleService: () => parcelleServiceStub,
          alertService: () => new AlertService(),

          historiqueService: () =>
            sinon.createStubInstance<HistoriqueService>(HistoriqueService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          grandeurService: () =>
            sinon.createStubInstance<GrandeurService>(GrandeurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          affectationService: () =>
            sinon.createStubInstance<AffectationService>(AffectationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          fermService: () =>
            sinon.createStubInstance<FermService>(FermService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          plantageService: () =>
            sinon.createStubInstance<PlantageService>(PlantageService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          typeSolService: () =>
            sinon.createStubInstance<TypeSolService>(TypeSolService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          alerteService: () =>
            sinon.createStubInstance<AlerteService>(AlerteService, {
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
        comp.parcelle = entity;
        parcelleServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(parcelleServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.parcelle = entity;
        parcelleServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(parcelleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundParcelle = { id: 123 };
        parcelleServiceStub.find.resolves(foundParcelle);
        parcelleServiceStub.retrieve.resolves([foundParcelle]);

        // WHEN
        comp.beforeRouteEnter({ params: { parcelleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.parcelle).toBe(foundParcelle);
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
