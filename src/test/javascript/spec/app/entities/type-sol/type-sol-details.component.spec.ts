/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TypeSolDetailComponent from '@/entities/type-sol/type-sol-details.vue';
import TypeSolClass from '@/entities/type-sol/type-sol-details.component';
import TypeSolService from '@/entities/type-sol/type-sol.service';
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
  describe('TypeSol Management Detail Component', () => {
    let wrapper: Wrapper<TypeSolClass>;
    let comp: TypeSolClass;
    let typeSolServiceStub: SinonStubbedInstance<TypeSolService>;

    beforeEach(() => {
      typeSolServiceStub = sinon.createStubInstance<TypeSolService>(TypeSolService);

      wrapper = shallowMount<TypeSolClass>(TypeSolDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { typeSolService: () => typeSolServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTypeSol = { id: 123 };
        typeSolServiceStub.find.resolves(foundTypeSol);

        // WHEN
        comp.retrieveTypeSol(123);
        await comp.$nextTick();

        // THEN
        expect(comp.typeSol).toBe(foundTypeSol);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTypeSol = { id: 123 };
        typeSolServiceStub.find.resolves(foundTypeSol);

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
