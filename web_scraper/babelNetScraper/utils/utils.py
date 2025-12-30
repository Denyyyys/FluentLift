import csv
import json

def filter_words_by_pos(
    input_csv_path: str,
    output_csv_path: str,
    allowed_pos=None
):
    if allowed_pos is None:
        allowed_pos = {"verb", "noun", "adverb", "adjective"}

    filtered_rows = []

    with open(input_csv_path, newline="", encoding="utf-8") as infile:
        reader = csv.reader(infile)
        header = next(reader)  # keep header

        for row in reader:
            if len(row) != 3:
                continue  # or raise error if you prefer

            word, level, pos = row
            if pos.strip().lower() in allowed_pos:
                filtered_rows.append(row)

    with open(output_csv_path, "w", newline="", encoding="utf-8") as outfile:
        writer = csv.writer(outfile)
        writer.writerow(header)
        writer.writerows(filtered_rows)


def count_elements_in_json(json_file_path):
    with open(json_file_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    if not isinstance(data, list):
        raise ValueError("JSON root is not an array")

    print(f"Number of elements: {len(data)}")


count_elements_in_json("/home/denys/langApp/web_scraper/data/synsets/synsets.json")

# filter_words_by_pos("/home/denys/langApp/web_scraper/data/oxford300/words_english_all.csv", 
#                     "/home/denys/langApp/web_scraper/data/oxford300/words_english_all_filtered.csv")
