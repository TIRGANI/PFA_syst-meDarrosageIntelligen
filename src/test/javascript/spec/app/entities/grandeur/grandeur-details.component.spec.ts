/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import GrandeurDetailComponent from '@/entities/grandeur/grandeur-details.vue';
import GrandeurClass from '@/entities/grandeur/grandeur-details.component';
import GrandeurService from '@/entities/grandeur/grandeur.service';
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
  describe('Grandeur Management Detail Component', () => {
    let wrapper: Wrapper<GrandeurClass>;
    let comp: GrandeurClass;
    let grandeurServiceStub: SinonStubbedInstance<GrandeurService>;

    beforeEach(() => {
      grandeurServiceStub = sinon.createStubInstance<GrandeurService>(GrandeurService);

      wrapper = shallowMount<GrandeurClass>(GrandeurDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { grandeurService: () => grandeurServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGrandeur = { id: 123 };
        grandeurServiceStub.find.resolves(foundGrandeur);

        // WHEN
        comp.retrieveGrandeur(123);
        await comp.$nextTick();

        // THEN
        expect(comp.grandeur).toBe(foundGrandeur);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGrandeur = { id: 123 };
        grandeurServiceStub.find.resolves(foundGrandeur);

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
