/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import BracheUpdateComponent from '@/entities/brache/brache-update.vue';
import BracheClass from '@/entities/brache/brache-update.component';
import BracheService from '@/entities/brache/brache.service';

import CapteurService from '@/entities/capteur/capteur.service';

import BoitierService from '@/entities/boitier/boitier.service';
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
  describe('Brache Management Update Component', () => {
    let wrapper: Wrapper<BracheClass>;
    let comp: BracheClass;
    let bracheServiceStub: SinonStubbedInstance<BracheService>;

    beforeEach(() => {
      bracheServiceStub = sinon.createStubInstance<BracheService>(BracheService);

      wrapper = shallowMount<BracheClass>(BracheUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          bracheService: () => bracheServiceStub,
          alertService: () => new AlertService(),

          capteurService: () =>
            sinon.createStubInstance<CapteurService>(CapteurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          boitierService: () =>
            sinon.createStubInstance<BoitierService>(BoitierService, {
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
        comp.brache = entity;
        bracheServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(bracheServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.brache = entity;
        bracheServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(bracheServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBrache = { id: 123 };
        bracheServiceStub.find.resolves(foundBrache);
        bracheServiceStub.retrieve.resolves([foundBrache]);

        // WHEN
        comp.beforeRouteEnter({ params: { bracheId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.brache).toBe(foundBrache);
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
