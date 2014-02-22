/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.api.test.syntax;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.api.test.baseclasses.AbstractAxiomsRoundTrippingTestCase;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.rdf.rdfxml.parser.OWLRDFXMLParserSAXException;
import org.semanticweb.owlapi.rdf.rdfxml.parser.RDFXMLParser;

/** @author Matthew Horridge, The University of Manchester, Information Management
 *         Group
 * @since 3.0.0 */
@SuppressWarnings("javadoc")
public class RelativeURITestCase extends AbstractAxiomsRoundTrippingTestCase {
    @Override
    protected Set<? extends OWLAxiom> createAxioms()
            throws OWLOntologyCreationException {
        OWLOntology ont = getOWLOntology("Ont");
        OWLClass cls = Class(IRI(ont.getOntologyID().getOntologyIRI()
                + "/Office"));
        Set<OWLAxiom> axs = new HashSet<OWLAxiom>();
        axs.add(Declaration(cls));
        return axs;
    }

    @Test
    public void shouldThrowMeaningfulException()
            throws OWLOntologyCreationException, IOException {
        expectedException.expect(OWLRDFXMLParserSAXException.class);
        expectedException
                .expectMessage("[line=1:column=378] IRI 'http://example.com/#1#2' cannot be resolved against current base IRI ");
        expectedException
                .expectMessage(" reason is: Illegal character in fragment at index 21: http://example.com/#1#2");
        String RDFCONTENT = ""
                + "<rdf:RDF"
                + "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""
                + "    xmlns:owl=\"http://www.w3.org/2002/07/owl#\""
                + "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\""
                + "    xmlns=\"http://example.org/rdfxmlparserbug#\""
                + "    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">"
                + "  <owl:Ontology rdf:about=\"http://example.org/rdfxmlparserbug\"/>"
                + "  <owl:Thing rdf:about=\"http://example.com/#1#2\">"
                + "    <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#NamedIndividual\"/>"
                + "  </owl:Thing>" + "</rdf:RDF>";
        InputStream in = new ByteArrayInputStream(RDFCONTENT.getBytes());
        OWLOntology ontology = m.createOntology();
        RDFXMLParser parser = new RDFXMLParser();
        parser.parse(new StreamDocumentSource(in), ontology);
    }
}
