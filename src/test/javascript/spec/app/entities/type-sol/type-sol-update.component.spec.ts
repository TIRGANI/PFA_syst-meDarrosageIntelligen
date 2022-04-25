/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TypeSolUpdateComponent from '@/entities/type-sol/type-sol-update.vue';
import TypeSolClass from '@/entities/type-sol/type-sol-update.component';
import TypeSolService from '@/entities/type-sol/type-sol.service';

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
  describe('TypeSol Management Update Component', () => {
    let wrapper: Wrapper<TypeSolClass>;
    let comp: TypeSolClass;
    let typeSolServiceStub: SinonStubbedInstance<TypeSolService>;

    beforeEach(() => {
      typeSolServiceStub = sinon.createStubInstance<TypeSolService>(TypeSolService);

      wrapper = shallowMount<TypeSolClass>(TypeSolUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          typeSolService: () => typeSolServiceStub,
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
        comp.typeSol = entity;
        typeSolServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(typeSolServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.typeSol = entity;
        typeSolServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(typeSolServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTypeSol = { id: 123 };
        typeSolServiceStub.find.resolves(foundTypeSol);
        typeSolServiceStub.retrieve.resolves([foundTypeSol]);

        // WHEN
        comp.beforeRouteEnter({ params: { typeSolId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.typeSol).toBe(foundTypeSol);
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
