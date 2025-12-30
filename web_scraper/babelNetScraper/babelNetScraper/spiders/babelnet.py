import scrapy
import csv
from scrapy.crawler import CrawlerProcess
from scrapy.loader import ItemLoader
import logging
from scrapy.http import Response

import csv

def get_words_with_pos(filename, toSkip, n):
    """
    Reads a CSV file (skipping the header),
    skips the first `toSkip` data rows,
    then reads the next `n` rows and returns
    a list of tuples (word, pos).
    """
    result = []

    with open(filename, newline="", encoding="utf-8") as csvfile:
        reader = csv.reader(csvfile)
        next(reader)  # skip header

        skipped = 0
        words_left = n

        for row in reader:
            if skipped < toSkip:
                skipped += 1
                continue

            if words_left <= 0:
                break

            result.append((row[0], row[2]))
            words_left -= 1

    return result



def create_query_url(word, lang='EN', transLang1='PL', translLang2='UK'):
    return f"https://babelnet.org/search?word={word}&lang={lang}&transLang={transLang1}&transLang={translLang2}"


class BabelnetSpider(scrapy.Spider):
    name = "babelnet"
    allowed_domains = ["babelnet.org"]
    languages = ["EN", "PL", "UK"]

    def start_requests(self):
        oxford3000_file_path = "/home/denys/langApp/web_scraper/data/oxford300/words_english_all_filtered.csv"
        number_of_words = 20
        toSkip = 180
        words = get_words_with_pos(oxford3000_file_path, toSkip, number_of_words)
        # words = [('accompany', 'verb'), ('accident', 'noun')]
        # words = [('affair', 'noun')]
        urls = [create_query_url(word) for word, _ in words]

        for (lemma, pos), url in zip(words, urls):
            yield scrapy.Request(
                url=url,
                callback=self.parse,
                meta={"lemma": lemma, "pos": pos}
            )


    def parse(self, response: Response):
        lemma = response.meta["lemma"]
        pos = response.meta["pos"]
        try:
            pos_results = response.css("div.pos-results")
            for pos_result in pos_results:
                container_pos = pos_result.css("div.pos-header span.pos::text").get().lower()
                if container_pos != pos:
                    continue
            
                synsets = pos_result.css("div.synset")
                synset_found = False
                for synset_container in synsets:
                    metadata_text_list = synset_container.css("div.meta ::text").getall()
                    
                    if "Named Entity" in metadata_text_list:
                        continue
                    
                    if synset_found:
                        break

                    # synset_link_element = synset_container.css("a.id").get()
                    # synset_url = response.urljoin(synset_link_element)
                    synset_found = True
                    yield response.follow(
                        # synset_url, 
                        synset_container.css("a.id::attr(href)").get(),
                        callback=self.parse_single_synset, 
                        meta={"lemma": lemma, "pos": pos}
                    )

                if not synset_found:
                    logging.warning(f"Not found synset, which is not 'Named Entity' for word: {lemma}.")

        except Exception as e:
            logging.error(f"Unexpected error processing in parse word {lemma}: {str(e)}")


    def parse_single_synset(self, response: Response):
        lemma = response.meta["lemma"]
        pos = response.meta["pos"]
        try:
            synset_info = {}
            synset_info["pos"] = pos
            for language in self.languages:
                synset_info[language] = {}
                synset_info[language]["lemma"] = ""
                synset_info[language]["synonyms"] = []
                synset_info[language]["definition"] = ""
                synset_info[language]["examples"] = []

            synset_info["EN"]["lemma"] = lemma

            # container for data for English definition
            dictionary_container = response.css("div.dictionary")
            if not dictionary_container:
                logging.warning(f"No dictionary container for {lemma}")
                return
            
            synset_id = dictionary_container.css("div.meta div.id::text").get()
            synset_info["synset_id"] = synset_id
            
            english_synonyms = dictionary_container.css('span.synonim[data-lemma-type="HIGH_QUALITY"]::text').getall()
            english_synonyms = [s.strip() for s in english_synonyms if s.strip() and s.strip() != lemma]
            synset_info["EN"]["synonyms"] = english_synonyms

            english_definition_container = response.css("div.definition-container div.definition")[0]
            definition_text = english_definition_container.xpath(".//text()[not(ancestor::a[@class='source'])]").getall()

            clean_definition = " ".join(t.strip() for t in definition_text if t.strip())
            synset_info["EN"]["definition"] = clean_definition

            # examples section
            example_containers = response.css("div.example-by-language .language-row")
            for example_container in example_containers:
                current_language = example_container.css("::attr(data-language)").get()
                if current_language not in self.languages:
                    continue
                
                examples = example_container.css("div.example::text").getall()
                examples = [s.strip() for s in examples if s.strip()]
                synset_info[current_language]["examples"] = examples


            translations_container = response.css("div.translation-by-language")
            language_rows = translations_container.css(".language-row")

            for language_row in language_rows:
                current_language = language_row.css("::attr(data-language)").get()
                if current_language not in self.languages:
                    continue

                synonims = language_row.css('span.synonim[data-lemma-type="HIGH_QUALITY"]::text').getall()
                synonims = [s.strip() for s in synonims if s.strip()]
                if synonims:
                    synset_info[current_language]["lemma"] = synonims[0]
                    synset_info[current_language]["synonyms"] = synonims[1:]


            definition_containers_by_language = response.css("div.tabs div.definition-by-language div.language-row")
            for definition_container in definition_containers_by_language:
                current_language = definition_container.css("::attr(data-language)").get()
                if current_language not in self.languages or current_language == "EN":
                    continue
                
                definition = definition_container.css("div.definition")[0]
                definition_text = definition.xpath(".//text()[not(ancestor::a[@class='source'])]").getall()
                clean_definition = " ".join(t.strip() for t in definition_text if t.strip())

                synset_info[current_language]["definition"] = clean_definition

            yield synset_info

        except Exception as e:
            logging.error(f"Unexpected error processing in parse_synset word {lemma}: {str(e)}")


# process = CrawlerProcess()

# process.crawl(BabelnetSpider)
# process.start()
