/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esiptestbed.mudrod.metadata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;

import esiptestbed.mudrod.discoveryengine.DiscoveryStepAbstract;
import esiptestbed.mudrod.driver.ESDriver;
import esiptestbed.mudrod.driver.SparkDriver;
import esiptestbed.mudrod.metadata.structure.MetadataExtractor;
import esiptestbed.mudrod.utils.SVDUtil;


public class MetadataSVDAnalyzer extends DiscoveryStepAbstract implements Serializable {
	public MetadataSVDAnalyzer(Map<String, String> config, ESDriver es, SparkDriver spark) {
		super(config, es, spark);
		// TODO Auto-generated constructor stub
	}
	
	public MetadataSVDAnalyzer() {
		super(null, null, null);
	}

	@Override
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object execute() {
		// TODO Auto-generated method stub
		try {
			SVDUtil svdUtil = new SVDUtil(config, es, spark);
			
			MetadataExtractor extractor = new MetadataExtractor();
			JavaPairRDD<String, List<String>> metadataTermsRDD = extractor.loadMetadata(this.es, this.spark.sc, config.get("indexName"),config.get("metadataType"));
			int svdDimension = Integer.parseInt(config.get("metadataSVDDimension"));
			
			svdUtil.buildSVDMatrix(metadataTermsRDD,svdDimension);
			svdUtil.CalSimilarity();
			svdUtil.insertLinkageToES(config.get("indexName"),config.get("metadataSimilarity"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
