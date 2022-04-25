/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import GrandeurUpdateComponent from '@/entities/grandeur/grandeur-update.vue';
import GrandeurClass from '@/entities/grandeur/grandeur-update.component';
import GrandeurService from '@/entities/grandeur/grandeur.service';

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
  describe('Grandeur Management Update Component', () => {
    let wrapper: Wrapper<GrandeurClass>;
    let comp: GrandeurClass;
    let grandeurServiceStub: SinonStubbedInstance<GrandeurService>;

    beforeEach(() => {
      grandeurServiceStub = sinon.createStubInstance<GrandeurService>(GrandeurService);

      wrapper = shallowMount<GrandeurClass>(GrandeurUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          grandeurService: () => grandeurServiceStub,
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
        comp.grandeur = entity;
        grandeurServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(grandeurServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.grandeur = entity;
        grandeurServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(grandeurServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGrandeur = { id: 123 };
        grandeurServiceStub.find.resolves(foundGrandeur);
        grandeurServiceStub.retrieve.resolves([foundGrandeur]);

        // WHEN
        comp.beforeRouteEnter({ params: { grandeurId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.grandeur).toBe(foundGrandeur);
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
