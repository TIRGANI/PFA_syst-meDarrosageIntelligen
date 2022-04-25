<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="grandeur">
        <h2 class="jh-entity-heading" data-cy="grandeurDetailsHeading">
          <span v-text="$t('pfaApp.grandeur.detail.title')">Grandeur</span> {{ grandeur.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.grandeur.type')">Type</span>
          </dt>
          <dd>
            <span>{{ grandeur.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.grandeur.valeur')">Valeur</span>
          </dt>
          <dd>
            <span>{{ grandeur.valeur }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.grandeur.date')">Date</span>
          </dt>
          <dd>
            <span>{{ grandeur.date }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.grandeur.parcelle')">Parcelle</span>
          </dt>
          <dd>
            <div v-if="grandeur.parcelle">
              <router-link :to="{ name: 'ParcelleView', params: { parcelleId: grandeur.parcelle.id } }">{{
                grandeur.parcelle.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="grandeur.id" :to="{ name: 'GrandeurEdit', params: { grandeurId: grandeur.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./grandeur-details.component.ts"></script>
