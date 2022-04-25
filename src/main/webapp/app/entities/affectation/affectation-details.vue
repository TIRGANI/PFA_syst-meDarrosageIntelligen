<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="affectation">
        <h2 class="jh-entity-heading" data-cy="affectationDetailsHeading">
          <span v-text="$t('pfaApp.affectation.detail.title')">Affectation</span> {{ affectation.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.affectation.dateDebut')">Date Debut</span>
          </dt>
          <dd>
            <span>{{ affectation.dateDebut }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.affectation.dateFin')">Date Fin</span>
          </dt>
          <dd>
            <span>{{ affectation.dateFin }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.affectation.boitier')">Boitier</span>
          </dt>
          <dd>
            <div v-if="affectation.boitier">
              <router-link :to="{ name: 'BoitierView', params: { boitierId: affectation.boitier.id } }">{{
                affectation.boitier.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.affectation.parcelle')">Parcelle</span>
          </dt>
          <dd>
            <div v-if="affectation.parcelle">
              <router-link :to="{ name: 'ParcelleView', params: { parcelleId: affectation.parcelle.id } }">{{
                affectation.parcelle.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="affectation.id"
          :to="{ name: 'AffectationEdit', params: { affectationId: affectation.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./affectation-details.component.ts"></script>
